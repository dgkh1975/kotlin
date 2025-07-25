// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER

import kotlin.reflect.KProperty

class Delegate(val x: Int) {
    operator fun getValue(t: Any?, p: KProperty<*>): Int = x
}

class My {
    val x: Int by Delegate(this.foo())

    fun foo(): Int = x
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, nullableType, operator, primaryConstructor,
propertyDeclaration, propertyDelegate, starProjection, thisExpression */
