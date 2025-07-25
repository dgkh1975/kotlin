// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_VARIABLE

object Boo {}
class A {
    object Boo {}
}

fun foo() {
    val i1: Int = <!INITIALIZER_TYPE_MISMATCH!>Boo<!>
    val i2: Int = <!INITIALIZER_TYPE_MISMATCH!>A.Boo<!>
    useInt(<!ARGUMENT_TYPE_MISMATCH!>Boo<!>)
    useInt(<!ARGUMENT_TYPE_MISMATCH!>A.Boo<!>)
}
fun bar() {
    val i1: Int = <!INITIALIZER_TYPE_MISMATCH!>Unit<!>
    useInt(<!ARGUMENT_TYPE_MISMATCH!>Unit<!>)
}

fun useInt(i: Int) = i

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, localProperty, nestedClass, objectDeclaration,
propertyDeclaration */
