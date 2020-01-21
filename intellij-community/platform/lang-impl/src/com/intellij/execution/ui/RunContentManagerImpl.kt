// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.execution.ui

import com.intellij.execution.ExecutionBundle
import com.intellij.execution.Executor
import com.intellij.execution.KillableProcess
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.dashboard.RunDashboardManager
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.execution.ui.layout.impl.DockableGridContainerFactory
import com.intellij.ide.DataManager
import com.intellij.ide.impl.ContentManagerWatcher
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.RegisterToolWindowTask.Companion.closable
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ToolWindowManager.Companion.getInstance
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.AppUIUtil
import com.intellij.ui.content.*
import com.intellij.ui.docking.DockManager
import com.intellij.util.ObjectUtils
import com.intellij.util.SmartList
import com.intellij.util.ui.EmptyIcon
import com.intellij.util.ui.UIUtil
import gnu.trove.THashMap
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.function.Predicate
import javax.swing.Icon

private val LOG = logger<RunContentManagerImpl>()
private val EXECUTOR_KEY: Key<Executor> = Key.create("Executor")
private val CLOSE_LISTENER_KEY: Key<ContentManagerListener> = Key.create("CloseListener")

class RunContentManagerImpl(private val project: Project) : RunContentManager {
  private val toolWindowIdToBaseIcon: MutableMap<String, Icon> = THashMap()
  private val toolWindowIdZBuffer = ConcurrentLinkedDeque<String>()

  init {
    val containerFactory = DockableGridContainerFactory()
    DockManager.getInstance(project).register(DockableGridContainerFactory.TYPE, containerFactory, project)
    AppUIUtil.invokeOnEdt(Runnable { init() }, project.disposed)
  }

  companion object {
    val ALWAYS_USE_DEFAULT_STOPPING_BEHAVIOUR_KEY = Key.create<Boolean>("ALWAYS_USE_DEFAULT_STOPPING_BEHAVIOUR_KEY")

    @ApiStatus.Internal
    val TEMPORARY_CONFIGURATION_KEY: Key<RunnerAndConfigurationSettings> = Key.create("TemporaryConfiguration")

    fun copyContentAndBehavior(descriptor: RunContentDescriptor, contentToReuse: RunContentDescriptor?) {
      if (contentToReuse != null) {
        val attachedContent = contentToReuse.attachedContent
        if (attachedContent != null && attachedContent.isValid) {
          descriptor.setAttachedContent(attachedContent)
        }
        if (contentToReuse.isReuseToolWindowActivation) {
          descriptor.isActivateToolWindowWhenAdded = contentToReuse.isActivateToolWindowWhenAdded
        }
        descriptor.contentToolWindowId = contentToReuse.contentToolWindowId
        descriptor.isSelectContentWhenAdded = contentToReuse.isSelectContentWhenAdded
      }
    }

    fun isTerminated(content: Content): Boolean {
      val processHandler = getRunContentDescriptorByContent(content)?.processHandler ?: return true
      return processHandler.isProcessTerminated
    }

    @JvmStatic
    fun getRunContentDescriptorByContent(content: Content): RunContentDescriptor? {
      return content.getUserData(RunContentDescriptor.DESCRIPTOR_KEY)
    }

    fun getExecutorByContent(content: Content): Executor? = content.getUserData(EXECUTOR_KEY)

    private fun getRunContentByDescriptor(contentManager: ContentManager,
                                          descriptor: RunContentDescriptor): Content? {
      return contentManager.contents.firstOrNull { descriptor == getRunContentDescriptorByContent(it) }
    }

    private fun isAlive(contentManager: ContentManager): Boolean {
      return contentManager.contents.any {
        val descriptor = getRunContentDescriptorByContent(it)
        descriptor != null && isAlive(descriptor)
      }
    }

    private fun isAlive(descriptor: RunContentDescriptor): Boolean {
      val handler = descriptor.processHandler
      return handler != null && !handler.isProcessTerminated
    }
  }

  // must be called on EDT
  private fun init() {
    val dashboardManager = RunDashboardManager.getInstance(project)
    dashboardManager.updateDashboard(true)
    initToolWindow(null, dashboardManager.toolWindowId, dashboardManager.toolWindowIcon, dashboardManager.dashboardContentManager)
    project.messageBus.connect().subscribe(ToolWindowManagerListener.TOPIC, object : ToolWindowManagerListener {
      override fun stateChanged(toolWindowManager: ToolWindowManager) {
        toolWindowIdZBuffer.retainAll(toolWindowManager.toolWindowIdSet)
        val activeToolWindowId = toolWindowManager.activeToolWindowId
        if (activeToolWindowId != null && toolWindowIdZBuffer.remove(activeToolWindowId)) {
          toolWindowIdZBuffer.addFirst(activeToolWindowId)
        }
      }
    })
  }

  private fun registerToolWindow(executor: Executor, toolWindowManager: ToolWindowManager): ContentManager {
    val toolWindowId = executor.toolWindowId
    var toolWindow = toolWindowManager.getToolWindow(toolWindowId)
    if (toolWindow != null) {
      return toolWindow.contentManager
    }

    toolWindow = toolWindowManager.registerToolWindow(closable(toolWindowId))
    val contentManager = toolWindow.contentManager
    contentManager.addDataProvider(object : DataProvider {
      private var myInsideGetData = 0
      override fun getData(dataId: String): Any? {
        myInsideGetData++
        return try {
          if (PlatformDataKeys.HELP_ID.`is`(dataId)) {
            executor.helpId
          }
          else {
            if (myInsideGetData == 1) DataManager.getInstance().getDataContext(contentManager.component).getData(dataId) else null
          }
        }
        finally {
          myInsideGetData--
        }
      }
    })
    toolWindow.setIcon(executor.toolWindowIcon)
    ContentManagerWatcher.watchContentManager(toolWindow, contentManager)
    initToolWindow(executor, toolWindowId, executor.toolWindowIcon, contentManager)
    return contentManager
  }

  private fun initToolWindow(executor: Executor?, toolWindowId: String, toolWindowIcon: Icon, contentManager: ContentManager) {
    toolWindowIdToBaseIcon.put(toolWindowId, toolWindowIcon)
    contentManager.addContentManagerListener(object : ContentManagerListener {
      override fun selectionChanged(event: ContentManagerEvent) {
        if (event.operation != ContentManagerEvent.ContentOperation.add) {
          return
        }

        val content = event.content
        var contentExecutor = executor
        if (contentExecutor == null) {
          // Content manager contains contents related with different executors.
          // Try to get executor from content.
          contentExecutor = getExecutorByContent(content)
          // Must contain this user data since all content is added by this class.
          LOG.assertTrue(contentExecutor != null)
        }
        syncPublisher.contentSelected(getRunContentDescriptorByContent(content), contentExecutor!!)
        content.helpId = contentExecutor.helpId
      }
    })
    Disposer.register(contentManager, Disposable {
      contentManager.removeAllContents(true)
      toolWindowIdZBuffer.remove(toolWindowId)
      toolWindowIdToBaseIcon.remove(toolWindowId)
    })
    toolWindowIdZBuffer.addLast(toolWindowId)
  }

  private val syncPublisher: RunContentWithExecutorListener
    get() = project.messageBus.syncPublisher(RunContentManager.TOPIC)

  override fun toFrontRunContent(requestor: Executor, handler: ProcessHandler) {
    val descriptor = getDescriptorBy(handler, requestor) ?: return
    toFrontRunContent(requestor, descriptor)
  }

  override fun toFrontRunContent(requestor: Executor, descriptor: RunContentDescriptor) {
    ApplicationManager.getApplication().invokeLater(Runnable {
      val contentManager = getContentManagerForRunner(requestor, descriptor)
      val content = getRunContentByDescriptor(contentManager, descriptor)
      if (content != null) {
        contentManager.setSelectedContent(content)
        getInstance(project).getToolWindow(getToolWindowIdForRunner(requestor, descriptor))!!.show(null)
      }
    }, project.disposed)
  }

  override fun hideRunContent(executor: Executor, descriptor: RunContentDescriptor) {
    ApplicationManager.getApplication().invokeLater(Runnable {
      val toolWindow = getInstance(project).getToolWindow(getToolWindowIdForRunner(executor, descriptor))
      toolWindow?.hide(null)
    }, project.disposed)
  }

  override fun getSelectedContent(): RunContentDescriptor? {
    for (activeWindow in toolWindowIdZBuffer) {
      val contentManager = getContentManagerByToolWindowId(activeWindow) ?: continue
      val selectedContent = contentManager.selectedContent
                            ?: if (contentManager.contentCount == 0) {
                              // continue to the next window if the content manager is empty
                              continue
                            }
                            else {
                              // stop iteration over windows because there is some content in the window and the window is the last used one
                              break
                            }
      // here we have selected content
      return getRunContentDescriptorByContent(selectedContent)
    }
    return null
  }

  override fun removeRunContent(executor: Executor, descriptor: RunContentDescriptor): Boolean {
    val contentManager = getContentManagerForRunner(executor, descriptor)
    val content = getRunContentByDescriptor(contentManager, descriptor)
    return content != null && contentManager.removeContent(content, true)
  }

  override fun showRunContent(executor: Executor, descriptor: RunContentDescriptor) {
    showRunContent(executor, descriptor, descriptor.executionId)
  }

  private fun showRunContent(executor: Executor, descriptor: RunContentDescriptor, executionId: Long) {
    if (ApplicationManager.getApplication().isUnitTestMode) {
      return
    }

    val contentManager = getContentManagerForRunner(executor, descriptor)
    val toolWindowId = getToolWindowIdForRunner(executor, descriptor)
    val runDashboardManager = RunDashboardManager.getInstance(project)
    val reuseCondition = if (runDashboardManager.toolWindowId == toolWindowId) runDashboardManager.reuseCondition else null
    val oldDescriptor = chooseReuseContentForDescriptor(contentManager, descriptor, executionId, descriptor.displayName, reuseCondition)
    val content: Content?
    if (oldDescriptor == null) {
      content = createNewContent(descriptor, executor)
    }
    else {
      content = oldDescriptor.attachedContent!!
      syncPublisher.contentRemoved(oldDescriptor, executor)
      Disposer.dispose(oldDescriptor) // is of the same category, can be reused
    }
    content.executionId = executionId
    content.component = descriptor.component
    content.setPreferredFocusedComponent(descriptor.preferredFocusComputable)
    content.putUserData(RunContentDescriptor.DESCRIPTOR_KEY, descriptor)
    content.putUserData(EXECUTOR_KEY, executor)
    content.displayName = descriptor.displayName
    descriptor.setAttachedContent(content)
    val toolWindow = getInstance(project).getToolWindow(toolWindowId)
    val processHandler = descriptor.processHandler
    if (processHandler != null) {
      val processAdapter: ProcessAdapter = object : ProcessAdapter() {
        override fun startNotified(event: ProcessEvent) {
          UIUtil.invokeLaterIfNeeded {
            content.icon = ExecutionUtil.getLiveIndicator(descriptor.icon)
            toolWindow!!.setIcon(ExecutionUtil.getLiveIndicator(
              toolWindowIdToBaseIcon[toolWindowId]))
          }
        }

        override fun processTerminated(event: ProcessEvent) {
          ApplicationManager.getApplication().invokeLater {
            val manager = getContentManagerByToolWindowId(
              toolWindowId) ?: return@invokeLater
            val alive = isAlive(
              manager)
            setToolWindowIcon(alive,
                              toolWindow!!)
            val icon = descriptor.icon
            content.icon = if (icon == null) executor.disabledIcon
            else IconLoader.getTransparentIcon(
              icon)
          }
        }
      }
      processHandler.addProcessListener(processAdapter)
      val disposer = content.disposer
      if (disposer != null) {
        Disposer.register(disposer, Disposable { processHandler.removeProcessListener(processAdapter) })
      }
    }
    if (oldDescriptor == null) {
      contentManager.addContent(content)
      val listener = CloseListener(
        content, executor)
      content.putUserData(CLOSE_LISTENER_KEY, listener)
    }
    if (descriptor.isSelectContentWhenAdded /* also update selection when reused content is already selected  */
        || oldDescriptor != null && contentManager.isSelected(content)) {
      content.manager!!.setSelectedContent(content)
    }
    if (!descriptor.isActivateToolWindowWhenAdded) {
      return
    }
    ApplicationManager.getApplication().invokeLater(Runnable {
      val window = getInstance(
        project).getToolWindow(
        toolWindowId)
      // let's activate tool window, but don't move focus
      //
      // window.show() isn't valid here, because it will not
      // mark the window as "last activated" windows and thus
      // some action like navigation up/down in stacktrace wont
      // work correctly
      window!!.activate(
        descriptor.activationCallback,
        descriptor.isAutoFocusContent,
        descriptor.isAutoFocusContent)
    }, project.disposed)
  }

  private fun getContentManagerByToolWindowId(toolWindowId: String): ContentManager? {
    val toolWindow = getInstance(project).getToolWindow(toolWindowId) ?: return null
    return toolWindow.contentManager
  }

  override fun getReuseContent(executionEnvironment: ExecutionEnvironment): RunContentDescriptor? {
    if (ApplicationManager.getApplication().isUnitTestMode) {
      return null
    }
    val contentToReuse = executionEnvironment.contentToReuse
    if (contentToReuse != null) {
      return contentToReuse
    }
    val toolWindowId = getContentDescriptorToolWindowId(executionEnvironment)
    val contentManager: ContentManager
    contentManager = if (toolWindowId == null) {
      getContentManagerForRunner(executionEnvironment.executor, null)
    }
    else {
      getOrCreateContentManagerForToolWindow(toolWindowId, executionEnvironment.executor)
    }
    val runDashboardManager = RunDashboardManager.getInstance(project)
    val reuseCondition = if (runDashboardManager.toolWindowId == toolWindowId) runDashboardManager.reuseCondition else null
    return chooseReuseContentForDescriptor(contentManager, null, executionEnvironment.executionId,
                                           executionEnvironment.toString(), reuseCondition)
  }

  override fun findContentDescriptor(requestor: Executor,
                                     handler: ProcessHandler): RunContentDescriptor? {
    return getDescriptorBy(handler, requestor)
  }

  override fun showRunContent(info: Executor,
                              descriptor: RunContentDescriptor,
                              contentToReuse: RunContentDescriptor?) {
    copyContentAndBehavior(descriptor, contentToReuse)
    showRunContent(info, descriptor, descriptor.executionId)
  }

  private fun getContentManagerForRunner(executor: Executor,
                                         descriptor: RunContentDescriptor?): ContentManager {
    return getOrCreateContentManagerForToolWindow(getToolWindowIdForRunner(executor, descriptor), executor)
  }

  private fun getOrCreateContentManagerForToolWindow(id: String,
                                                     executor: Executor): ContentManager {
    return getContentManagerByToolWindowId(id) ?: return registerToolWindow(executor, getInstance(project))
  }

  override fun getToolWindowByDescriptor(descriptor: RunContentDescriptor): ToolWindow? {
    val toolWindowManager = getInstance(project)
    val toolWindowId = descriptor.contentToolWindowId
    if (toolWindowId != null) {
      return toolWindowManager.getToolWindow(toolWindowId)
    }
    for (executor in Executor.EXECUTOR_EXTENSION_NAME.extensionList) {
      val toolWindow = toolWindowManager.getToolWindow(executor.toolWindowId)
      if (toolWindow != null) {
        if (getRunContentByDescriptor(toolWindow.contentManager, descriptor) != null) {
          return toolWindow
        }
      }
    }
    return null
  }

  override fun getAllDescriptors(): List<RunContentDescriptor> {
    val toolWindowManager = getInstance(project)
    val descriptors: MutableList<RunContentDescriptor> = SmartList()
    for (executor in Executor.EXECUTOR_EXTENSION_NAME.extensionList) {
      val toolWindow = toolWindowManager.getToolWindow(executor.id) ?: continue
      for (content in toolWindow.contentManager.contents) {
        val descriptor = getRunContentDescriptorByContent(content)
        if (descriptor != null) {
          descriptors.add(descriptor)
        }
      }
    }
    return descriptors
  }

  override fun selectRunContent(descriptor: RunContentDescriptor) {
    val toolWindowManager = getInstance(project)
    for (executor in Executor.EXECUTOR_EXTENSION_NAME.extensionList) {
      val toolWindow = toolWindowManager.getToolWindow(executor.id) ?: continue
      val contentManager = toolWindow.contentManager
      val content = getRunContentByDescriptor(contentManager, descriptor)
      if (content != null) {
        contentManager.setSelectedContent(content)
      }
    }
  }

  override fun getContentDescriptorToolWindowId(configuration: RunConfiguration?): String? {
    if (configuration != null) {
      val runDashboardManager = RunDashboardManager.getInstance(project)
      if (runDashboardManager.isShowInDashboard(configuration)) {
        return runDashboardManager.toolWindowId
      }
    }
    return null
  }

  override fun getToolWindowIdByEnvironment(executionEnvironment: ExecutionEnvironment): String {
    // TODO [konstantin.aleev] Check remaining places where Executor.getToolWindowId() is used
    // Also there are some places where ToolWindowId.RUN or ToolWindowId.DEBUG are used directly.
    // For example, HotSwapProgressImpl.NOTIFICATION_GROUP. All notifications for this group is shown in Debug tool window,
    // however such notifications should be shown in Run Dashboard tool window, if run content is redirected to Run Dashboard tool window.
    val toolWindowId = getContentDescriptorToolWindowId(executionEnvironment)
    return toolWindowId ?: executionEnvironment.executor.toolWindowId
  }

  private fun getDescriptorBy(handler: ProcessHandler, runnerInfo: Executor): RunContentDescriptor? {
    val contents = mutableListOf<Content>()
    contents.addAll(getContentManagerForRunner(runnerInfo, null).contents)
    contents.addAll(getContentManagerByToolWindowId(RunDashboardManager.getInstance(project).toolWindowId)!!.contents)
    for (content in contents) {
      val runContentDescriptor = getRunContentDescriptorByContent(content)!!
      if (runContentDescriptor.processHandler === handler) {
        return runContentDescriptor
      }
    }
    return null
  }

  fun moveContent(executor: Executor, descriptor: RunContentDescriptor) {
    val content = descriptor.attachedContent ?: return
    val oldContentManager = content.manager
    val newContentManager = getContentManagerForRunner(executor, descriptor)
    if (oldContentManager == null || oldContentManager === newContentManager) return
    val listener = content.getUserData(CLOSE_LISTENER_KEY)
    if (listener != null) {
      oldContentManager.removeContentManagerListener(listener)
    }
    oldContentManager.removeContent(content, false)
    if (isAlive(descriptor)) {
      if (!isAlive(oldContentManager)) {
        updateToolWindowIcon(oldContentManager, false)
      }
      if (!isAlive(newContentManager)) {
        updateToolWindowIcon(newContentManager, true)
      }
    }
    newContentManager.addContent(content)
    if (listener != null) {
      newContentManager.addContentManagerListener(listener)
    }
  }

  private fun updateToolWindowIcon(contentManager: ContentManager, alive: Boolean) {
    val dataProvider = DataManager.getDataProvider(contentManager.component)
    val toolWindow = if (dataProvider == null) null
    else PlatformDataKeys.TOOL_WINDOW.getData(dataProvider)
    if (toolWindow != null && toolWindow.contentManager == contentManager) {
      setToolWindowIcon(alive, toolWindow)
    }
  }

  private fun setToolWindowIcon(alive: Boolean, toolWindow: ToolWindow) {
    val base = toolWindowIdToBaseIcon[toolWindow.id]
    toolWindow.setIcon(if (alive) ExecutionUtil.getLiveIndicator(base)
                       else ObjectUtils.notNull(base, EmptyIcon.ICON_13))
  }

  private inner class CloseListener(content: Content,
                                    private val myExecutor: Executor) : BaseContentCloseListener(content,
                                                                                                 project) {
    override fun disposeContent(content: Content) {
      try {
        val descriptor = getRunContentDescriptorByContent(content)
        syncPublisher.contentRemoved(descriptor, myExecutor)
        if (descriptor != null) {
          Disposer.dispose(descriptor)
        }
      }
      finally {
        content.release()
      }
    }

    override fun closeQuery(content: Content, projectClosing: Boolean): Boolean {
      val descriptor = getRunContentDescriptorByContent(content) ?: return true
      val processHandler = descriptor.processHandler
      if (processHandler == null || processHandler.isProcessTerminated) {
        return true
      }
      val sessionName = descriptor.displayName
      val killable = processHandler is KillableProcess && (processHandler as KillableProcess).canKillProcess()
      val task: WaitForProcessTask = object : WaitForProcessTask(processHandler, sessionName, projectClosing, project) {
        override fun onCancel() {
          if (killable && !processHandler.isProcessTerminated) {
            (processHandler as KillableProcess).killProcess()
          }
        }
      }
      if (killable) {
        val cancelText = ExecutionBundle.message("terminating.process.progress.kill")
        task.cancelText = cancelText
        task.cancelTooltipText = cancelText
      }
      return askUserAndWait(processHandler, sessionName, task)
    }
  }
}

private fun chooseReuseContentForDescriptor(contentManager: ContentManager,
                                            descriptor: RunContentDescriptor?,
                                            executionId: Long,
                                            preferredName: String?,
                                            reuseCondition: Predicate<in Content>?): RunContentDescriptor? {
  var content: Content? = null
  if (descriptor != null) {
    //Stage one: some specific descriptors (like AnalyzeStacktrace) cannot be reused at all
    if (descriptor.isContentReuseProhibited) {
      return null
    }
    // stage two: try to get content from descriptor itself
    val attachedContent = descriptor.attachedContent
    if (attachedContent != null && attachedContent.isValid
        && contentManager.getIndexOfContent(attachedContent) != -1 && (Comparing.equal(descriptor.displayName,
                                                                                       attachedContent.displayName) || !attachedContent.isPinned)) {
      content = attachedContent
    }
  }

  // stage three: choose the content with name we prefer
  if (content == null) {
    content = getContentFromManager(contentManager, preferredName, executionId, reuseCondition)
  }
  if (content == null || !RunContentManagerImpl.isTerminated(content) || content.executionId == executionId && executionId != 0L) {
    return null
  }

  val oldDescriptor = RunContentManagerImpl.getRunContentDescriptorByContent(content) ?: return null
  if (oldDescriptor.isContentReuseProhibited) {
    return null
  }
  if (descriptor == null || oldDescriptor.reusePolicy.canBeReusedBy(descriptor)) {
    return oldDescriptor
  }
  return null
}

private fun getContentFromManager(contentManager: ContentManager,
                                  preferredName: String?,
                                  executionId: Long,
                                  reuseCondition: Predicate<in Content>?): Content? {
  val contents = contentManager.contents.toMutableList()
  val first = contentManager.selectedContent
  if (first != null && contents.remove(first)) { //selected content should be checked first
    contents.add(0, first)
  }
  if (preferredName != null) {
    // try to match content with specified preferred name
    for (c in contents) {
      if (canReuseContent(c, executionId) && preferredName == c.displayName) {
        return c
      }
    }
  }

  // return first "good" content
  return contents.firstOrNull {
    canReuseContent(it, executionId) && (reuseCondition == null || reuseCondition.test(it))
  }
}

private fun canReuseContent(c: Content, executionId: Long): Boolean {
  return !c.isPinned && RunContentManagerImpl.isTerminated(c) && !(c.executionId == executionId && executionId != 0L)
}

private fun getToolWindowIdForRunner(executor: Executor, descriptor: RunContentDescriptor?): String {
  return if (descriptor != null && descriptor.contentToolWindowId != null) {
    descriptor.contentToolWindowId!!
  }
  else {
    executor.toolWindowId
  }
}

private fun createNewContent(descriptor: RunContentDescriptor, executor: Executor): Content {
  val content = ContentFactory.SERVICE.getInstance().createContent(descriptor.component, descriptor.displayName, true)
  content.putUserData(ToolWindow.SHOW_CONTENT_ICON, java.lang.Boolean.TRUE)
  content.icon = descriptor.icon ?: executor.toolWindowIcon
  return content
}