/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.android.parcel

import org.jetbrains.kotlin.android.synthetic.AndroidComponentRegistrar
import org.jetbrains.kotlin.android.synthetic.test.addAndroidExtensionsRuntimeLibrary
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.codegen.CodegenTestCase
import org.jetbrains.kotlin.codegen.getClassFiles
import org.jetbrains.kotlin.utils.PathUtil
import org.jetbrains.org.objectweb.asm.ClassWriter
import org.jetbrains.org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.jetbrains.org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.jetbrains.org.objectweb.asm.Label
import org.jetbrains.org.objectweb.asm.Opcodes.*
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter
import org.junit.runner.JUnitCore
import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit

abstract class AbstractParcelBoxTest : CodegenTestCase() {
    companion object {
        val LIBRARY_KT = File("plugins/android-extensions/android-extensions-compiler/testData/parcel/boxLib.kt")

        private fun String.withoutAndroidPrefix(): String = removePrefix("studio.android.sdktools.")

        private fun getLayoutLibFile(property: String): File {
            val layoutLibFile = File(System.getProperty(property))
            if (!layoutLibFile.isFile) {
                error("Can't find jar file in $property system property")
            }
            return layoutLibFile
        }

        val layoutlibJar: File by lazy { getLayoutLibFile("layoutLib.path") }

        val layoutlibApiJar: File by lazy { getLayoutLibFile("layoutLibApi.path") }

        private val JUNIT_GENERATED_TEST_CLASS_BYTES by lazy { constructSyntheticTestClass() }
        private const val JUNIT_GENERATED_TEST_CLASS_FQNAME = "test.JunitTest"

        private fun constructSyntheticTestClass(): ByteArray {
            return with(ClassWriter(COMPUTE_MAXS or COMPUTE_FRAMES)) {
                visit(49, ACC_PUBLIC, JUNIT_GENERATED_TEST_CLASS_FQNAME.replace('.', '/'), null, "java/lang/Object", emptyArray())
                visitSource(null, null)

                with(visitAnnotation("Lorg/junit/runner/RunWith;", true)) {
                    visit("value", Type.getType("Lorg/robolectric/RobolectricTestRunner;"))
                    visitEnd()
                }

                with(visitAnnotation("Lorg/robolectric/annotation/Config;", true)) {
                    visit("sdk", intArrayOf(21))
                    visit("manifest", "--none")
                    visitEnd()
                }

                with(visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)) {
                    visitVarInsn(ALOAD, 0)
                    visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)

                    visitInsn(RETURN)
                    visitMaxs(-1, -1)
                    visitEnd()
                }

                with(visitMethod(ACC_PUBLIC, "test", "()V", null, null)) {
                    visitAnnotation("Lorg/junit/Test;", true).visitEnd()

                    val v = InstructionAdapter(this)

                    val assertionOk = Label()

                    v.invokestatic("test/TestKt", "box", "()Ljava/lang/String;", false) // -> ret
                    v.dup() // -> ret, ret
                    v.aconst("OK") // -> ret, ret, ok
                    v.invokevirtual("java/lang/String", "equals", "(Ljava/lang/Object;)Z", false) // -> ret, eq
                    v.ifne(assertionOk) // -> ret

                    val assertionErrorType = Type.getObjectType("java/lang/AssertionError")

                    v.anew(assertionErrorType) // -> ret, ae
                    v.dupX1() // -> ae, ret, ae
                    v.swap() // -> ae, ae, ret
                    v.invokespecial(assertionErrorType.internalName, "<init>", "(Ljava/lang/Object;)V", false) // -> ae
                    v.athrow()

                    v.visitLabel(assertionOk)
                    v.pop() // -> [empty]
                    v.areturn(Type.VOID_TYPE)

                    visitMaxs(-1, -1)
                    visitEnd()
                }

                visitEnd()
                toByteArray()
            }
        }
    }

    private fun getClasspathForTest(): List<File> {
        val kotlinRuntimeJar = PathUtil.kotlinPathsForIdeaPlugin.stdlibPath

        val robolectricClasspath = System.getProperty("robolectric.classpath")
            ?: throw RuntimeException("Unable to get a valid classpath from 'robolectric.classpath' property, please set it accordingly")
        val robolectricJars = robolectricClasspath.split(File.pathSeparator)
            .map { File(it) }
            .sortedBy { it.nameWithoutExtension }

        val junitCoreResourceName = JUnitCore::class.java.name.replace('.', '/') + ".class"
        val junitJar =
            File(JUnitCore::class.java.classLoader.getResource(junitCoreResourceName).file.substringAfter("file:").substringBeforeLast('!'))

        val androidExtensionsRuntimeJars = System.getProperty("androidExtensionsRuntime.classpath")?.split(File.pathSeparator)?.map(::File)
            ?: error("Unable to get a valid classpath from 'androidExtensionsRuntime.classpath' property")

        return listOf(kotlinRuntimeJar, layoutlibJar, layoutlibApiJar) + robolectricJars + junitJar + androidExtensionsRuntimeJars
    }

    override fun doMultiFileTest(wholeFile: File, files: List<TestFile>) {
        compile(files + TestFile(LIBRARY_KT.name, LIBRARY_KT.readText()))

        val javaBin = File(System.getProperty("java.home").takeIf { it.isNotEmpty() } ?: error("JAVA_HOME is not set"), "bin")
        val javaExe = File(javaBin, "java.exe").takeIf { it.exists() } ?: File(javaBin, "java")
        assert(javaExe.exists()) { "Can't find 'java' executable in $javaBin" }

        val libraryClasspath = getClasspathForTest()
        val dirForTestClasses = Files.createTempDirectory("parcel").toFile()

        fun writeClass(fqNameOrPath: String, bytes: ByteArray) {
            val path = if (fqNameOrPath.endsWith(".class")) fqNameOrPath else (fqNameOrPath.replace('.', '/') + ".class")
            File(dirForTestClasses, path).also { it.parentFile.mkdirs() }.writeBytes(bytes)
        }

        try {
            writeClass(JUNIT_GENERATED_TEST_CLASS_FQNAME, JUNIT_GENERATED_TEST_CLASS_BYTES)
            classFileFactory.getClassFiles().forEach { writeClass(it.relativePath, it.asByteArray()) }
            javaClassesOutputDirectory?.listFiles()?.forEach { writeClass(it.name, it.readBytes()) }

            val robolectricProperties = System.getProperties().propertyNames().asSequence()
                .map { it.toString() }.filter { it.startsWith("robolectric") }
                .map { "-D$it=${System.getProperty(it)}" }
                .toList()
                .toTypedArray()

            val process = ProcessBuilder(
                javaExe.absolutePath,
                "-ea",
                "-classpath",
                (libraryClasspath + dirForTestClasses).joinToString(File.pathSeparator),
                *robolectricProperties,
                JUnitCore::class.java.name,
                JUNIT_GENERATED_TEST_CLASS_FQNAME,
            ).start()

            val out = process.inputStream.bufferedReader().lineSequence().joinToString("\n")
            val err = process.errorStream.bufferedReader().lineSequence().joinToString("\n")

            process.waitFor(3, TimeUnit.MINUTES)

            if (process.exitValue() != 0) {
                println(out)
                println(err)

                throw AssertionError("Process exited with exit code ${process.exitValue()} \n" + classFileFactory.createText())
            }
        } finally {
            if (!dirForTestClasses.deleteRecursively()) {
                throw AssertionError("Unable to delete $dirForTestClasses")
            }
        }
    }

    override fun setupEnvironment(environment: KotlinCoreEnvironment) {
        AndroidComponentRegistrar.registerParcelExtensions(environment.project)
        addAndroidExtensionsRuntimeLibrary(environment)
        environment.updateClasspath(listOf(JvmClasspathRoot(layoutlibJar)))
    }

    override fun updateJavaClasspath(javaClasspath: MutableList<String>) {
        javaClasspath += layoutlibJar.path
    }
}
