// RUN_PIPELINE_TILL: FRONTEND
fun <T : Any?> foo(x: T) {
    if (x is String?) {
        x<!UNSAFE_CALL!>.<!>length

        if (x != null) {
            <!DEBUG_INFO_SMARTCAST!>x<!>.length
        }
    }

    if (x is String) {
        <!DEBUG_INFO_SMARTCAST!>x<!>.length
    }
}

/* GENERATED_FIR_TAGS: equalityExpression, functionDeclaration, ifExpression, intersectionType, isExpression,
nullableType, smartcast, typeConstraint, typeParameter */
