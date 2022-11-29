/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.dataframe.extensions

import org.jetbrains.kotlin.diagnostics.*
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirFunctionCallChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.dataframe.InterpretationErrorReporter
import org.jetbrains.kotlin.fir.expressions.FirFunctionCall
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlinx.dataframe.KotlinTypeFacadeImpl

class FirDataFrameAdditionalCheckers(session: FirSession) : FirAdditionalCheckersExtension(session) {
    override val expressionCheckers: ExpressionCheckers = object : ExpressionCheckers() {
        override val functionCallCheckers: Set<FirFunctionCallChecker> = setOf(Checker())
    }
}

class Checker : FirFunctionCallChecker() {
    companion object {
        val ERROR by error1<KtElement, String>(SourceElementPositioningStrategies.DEFAULT)
    }
    override fun check(expression: FirFunctionCall, context: CheckerContext, reporter: DiagnosticReporter) {
        with(KotlinTypeFacadeImpl(context.session)) {
            analyzeRefinedCallShape(expression, reporter = object : InterpretationErrorReporter {
                override var errorReported: Boolean = false

                override fun reportInterpretationError(call: FirFunctionCall, message: String) {
                    call.calleeReference.source
                    reporter.reportOn(call.source, ERROR, message, context)
                    errorReported = true
                }
            })
        }
    }
}
