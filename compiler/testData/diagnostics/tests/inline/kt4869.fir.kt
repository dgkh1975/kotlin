// RUN_PIPELINE_TILL: FRONTEND
inline fun foo(f: () -> Unit) {
    val ff = { f: () -> Unit ->
        f.invoke()
    }
    ff(<!USAGE_IS_NOT_INLINABLE!>f<!>)
}

/* GENERATED_FIR_TAGS: functionDeclaration, functionalType, inline, lambdaLiteral, localProperty, propertyDeclaration */
