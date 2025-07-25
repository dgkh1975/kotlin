// RUN_PIPELINE_TILL: FRONTEND
// CHECK_TYPE

interface A

interface B : A
operator fun B.compareTo(b: B) = if (this == b) 0 else 1

fun foo(a: A): Boolean {
    val result = (a as B) < <!DEBUG_INFO_SMARTCAST!>a<!>
    checkSubtype<B>(<!DEBUG_INFO_SMARTCAST!>a<!>)
    return result
}

fun bar(a: A, b: B): Boolean {
    val result = b < (a as B)
    checkSubtype<B>(<!DEBUG_INFO_SMARTCAST!>a<!>)
    return result
}

/* GENERATED_FIR_TAGS: asExpression, classDeclaration, comparisonExpression, equalityExpression,
funWithExtensionReceiver, functionDeclaration, functionalType, ifExpression, infix, integerLiteral, interfaceDeclaration,
localProperty, nullableType, operator, propertyDeclaration, smartcast, thisExpression, typeParameter, typeWithExtension */
