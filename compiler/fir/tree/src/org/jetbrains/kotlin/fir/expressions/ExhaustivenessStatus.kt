/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.expressions

import org.jetbrains.kotlin.fir.symbols.CallableId
import org.jetbrains.kotlin.name.ClassId

sealed class ExhaustivenessStatus {
    object Exhaustive : ExhaustivenessStatus()
    class NotExhaustive(val reasons: List<WhenMissingCase>) : ExhaustivenessStatus() {
        companion object {
            val NO_ELSE_BRANCH = NotExhaustive(listOf(WhenMissingCase.Unknown))
        }
    }
}

sealed class WhenMissingCase {
    object Unknown : WhenMissingCase()
    object NullIsMissing : WhenMissingCase()
    sealed class BooleanIsMissing(val value: Boolean) : WhenMissingCase() {
        object True : BooleanIsMissing(true)
        object False : BooleanIsMissing(false)
    }
    class IsTypeCheckIsMissing(val classId: ClassId) : WhenMissingCase()
    class EnumCheckIsMissing(val callableId: CallableId) : WhenMissingCase()
}

val FirWhenExpression.isExhaustive: Boolean
    get() = exhaustivenessStatus == ExhaustivenessStatus.Exhaustive
