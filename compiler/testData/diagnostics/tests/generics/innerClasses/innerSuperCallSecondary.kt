// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_PARAMETER
open class Super<T> {
    inner open class Inner {
    }
}

class Sub : Super<String>() {
    inner class SubInner : Super<String>.Inner {
        constructor()
        constructor(x: Int) : super() {}
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, inner, nullableType, secondaryConstructor, typeParameter */
