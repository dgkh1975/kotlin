// RUN_PIPELINE_TILL: FRONTEND
fun foo(u : Unit) : Int = 1

fun test() : Int {
    foo(<!ARGUMENT_TYPE_MISMATCH!>1<!>)
    val a : () -> Unit = {
        foo(<!ARGUMENT_TYPE_MISMATCH!>1<!>)
    }
    return 1 <!NONE_APPLICABLE!>-<!> "1"
}

class A() {
    val x : Int = <!INITIALIZER_TYPE_MISMATCH!>foo1(<!TOO_MANY_ARGUMENTS, UNRESOLVED_REFERENCE!>xx<!>)<!>
}

fun foo1() {}

/* GENERATED_FIR_TAGS: additiveExpression, classDeclaration, functionDeclaration, functionalType, integerLiteral,
lambdaLiteral, localProperty, primaryConstructor, propertyDeclaration, stringLiteral */
