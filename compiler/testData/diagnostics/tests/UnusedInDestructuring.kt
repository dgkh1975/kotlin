// RUN_PIPELINE_TILL: BACKEND
// DIAGNOSTICS: +UNUSED_VARIABLE

data class D(val x: Int, val y: Int, val z: Int)
fun foo(): Int {
    val (<!UNUSED_VARIABLE!>x<!>, y, z) = D(1, 2, 3)
    return y + z // x is not used, but we cannot do anything with it
}
fun bar(): Int {
    val (x, y, <!UNUSED_VARIABLE!>z<!>) = D(1, 2, 3)
    return y + x // z is not used
}

/* GENERATED_FIR_TAGS: additiveExpression, classDeclaration, data, destructuringDeclaration, functionDeclaration,
integerLiteral, localProperty, primaryConstructor, propertyDeclaration */
