// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_VARIABLE


fun a() {
    val x = 10
    foo(::<!UNSUPPORTED!>x<!>)
}

fun foo(a: Any) {}

fun test1(test2: () -> Unit = ::test2) {} // Resolve to function
private fun test2() {}
fun test3(test4: () -> Unit = ::<!UNSUPPORTED!>test4<!>) {}

fun test5(test6: (test: Test) -> Unit = Test::helper) {
    test6(Test())
}

class Test {
    fun helper() {}
}

/* GENERATED_FIR_TAGS: callableReference, classDeclaration, functionDeclaration, functionalType, integerLiteral,
localProperty, propertyDeclaration */
