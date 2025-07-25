// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// DIAGNOSTICS: -UNUSED_EXPRESSION
// See KT-27260

class A(val x: String?) {
    fun foo(other: A) {
        when {
            x == null && other.x == null -> "1"
            x<!UNSAFE_CALL!>.<!>length > 0 -> "2"
        }
    }
}

/* GENERATED_FIR_TAGS: andExpression, classDeclaration, comparisonExpression, equalityExpression, functionDeclaration,
integerLiteral, nullableType, primaryConstructor, propertyDeclaration, stringLiteral, whenExpression */
