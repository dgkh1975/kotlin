/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.services.sourceProviders

import org.jetbrains.kotlin.codegen.forTestCompile.ForTestCompileRuntime
import org.jetbrains.kotlin.test.directives.AdditionalFilesDirectives
import org.jetbrains.kotlin.test.directives.AdditionalFilesDirectives.SPEC_HELPERS
import org.jetbrains.kotlin.test.directives.model.DirectivesContainer
import org.jetbrains.kotlin.test.directives.model.RegisteredDirectives
import org.jetbrains.kotlin.test.model.TestFile
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.AdditionalSourceProvider
import org.jetbrains.kotlin.test.services.TestModuleStructure
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

class SpecHelpersSourceFilesProvider(testServices: TestServices, testType: String) : AdditionalSourceProvider(testServices) {
    private val helpersDirPath = ForTestCompileRuntime.transformTestDataPath("compiler/tests-spec/testData/$testType/helpers").absolutePath

    override val directiveContainers: List<DirectivesContainer> =
        listOf(AdditionalFilesDirectives)

    override fun produceAdditionalFiles(
        globalDirectives: RegisteredDirectives,
        module: TestModule,
        testModuleStructure: TestModuleStructure
    ): List<TestFile> {
        if (SPEC_HELPERS !in module.directives) return emptyList()
        return File(helpersDirPath).walkTopDown().mapNotNull {
            if (it.isDirectory) return@mapNotNull null
            it.toTestFile()
        }.toList()
    }
}
