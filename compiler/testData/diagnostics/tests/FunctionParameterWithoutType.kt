// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_ANONYMOUS_PARAMETER -UNUSED_VARIABLE
fun test(<!VALUE_PARAMETER_WITH_NO_TYPE_ANNOTATION!>a<!>, <!VALUE_PARAMETER_WITH_NO_TYPE_ANNOTATION!>b<!>, <!VALUE_PARAMETER_WITH_NO_TYPE_ANNOTATION!>c<!>) {

}

class A(a<!SYNTAX!><!>)

val bar = fun(<!CANNOT_INFER_PARAMETER_TYPE!>a<!>){}

val la = { <!CANNOT_INFER_PARAMETER_TYPE!>a<!> -> }
val las = { a: Int -> }

/* GENERATED_FIR_TAGS: anonymousFunction, classDeclaration, functionDeclaration, lambdaLiteral, primaryConstructor,
propertyDeclaration */
