// RUN_PIPELINE_TILL: FRONTEND
package aa

val a : Int = <!UNINITIALIZED_VARIABLE!>b<!>
val b : Int = a + <!UNINITIALIZED_VARIABLE!>b<!>

class C {
    val a : Int = <!UNINITIALIZED_VARIABLE!>b<!>
    val b : Int = a + <!UNINITIALIZED_VARIABLE!>b<!>
}

fun foo() {
    val a : Int
    <!UNINITIALIZED_VARIABLE!>a<!> + 1
    <!UNINITIALIZED_VARIABLE!>a<!> + 1
}

/* GENERATED_FIR_TAGS: additiveExpression, classDeclaration, functionDeclaration, integerLiteral, localProperty,
propertyDeclaration */
