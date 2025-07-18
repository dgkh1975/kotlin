// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// CHECK_TYPE
// DIAGNOSTICS: -UNUSED_PARAMETER
// FILE: A.java

public class A {
    public static Out<? super CharSequence> foo() { return null; }
    public static In<? extends CharSequence> bar() { return null; }
}

// FILE: main.kt

class Out<out E> {
    fun x(): E = null!!
}

class In<in F> {
    fun y(f: F) {}
}

fun test() {
    A.foo().x() checkType { _<Any?>() }
    A.bar().y(<!NULL_FOR_NONNULL_TYPE!>null<!>)
}

/* GENERATED_FIR_TAGS: checkNotNullCall, classDeclaration, flexibleType, funWithExtensionReceiver, functionDeclaration,
functionalType, in, infix, javaFunction, lambdaLiteral, nullableType, out, starProjection, typeParameter,
typeWithExtension */
