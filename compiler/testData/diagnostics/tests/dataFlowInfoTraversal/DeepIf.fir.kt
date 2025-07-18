// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -DEBUG_INFO_SMARTCAST
fun bar(x: Int) = x + 1

fun foo() {
    val x: Int? = null

    if (x != null) {
        bar(x)
        if (<!SENSELESS_COMPARISON!>x != null<!>) {
            bar(x)
            if (1 < 2) bar(x)
            if (1 > 2) bar(x)
        }
        if (<!SENSELESS_COMPARISON!>x == null<!>) {
            bar(x)
        }
        if (<!SENSELESS_COMPARISON!>x == null<!>) bar(x) else bar(x)
        bar(bar(x))
    } else if (<!SENSELESS_COMPARISON!>x == null<!>) {
        bar(<!ARGUMENT_TYPE_MISMATCH!>x<!>)
        if (<!SENSELESS_COMPARISON!>x != null<!>) {
            bar(x)
            if (<!SENSELESS_COMPARISON!>x == null<!>) bar(x)
            if (<!SENSELESS_COMPARISON!>x == null<!>) bar(x) else bar(x)
            bar(bar(x) + bar(x))
        } else if (<!SENSELESS_COMPARISON!>x == null<!>) {
            bar(<!ARGUMENT_TYPE_MISMATCH!>x<!>)
        }
    }

}

/* GENERATED_FIR_TAGS: additiveExpression, comparisonExpression, equalityExpression, functionDeclaration, ifExpression,
integerLiteral, localProperty, nullableType, propertyDeclaration, smartcast */
