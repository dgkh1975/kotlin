// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// MODULE: m1-common
// FILE: common.kt
expect class Foo {
    val foo: String

    fun bar(x: Int): Int
}

// MODULE: m2-jvm()()(m1-common)
// FILE: jvm.kt
actual class Foo {
    actual val foo: String = "JVM"

    actual fun bar(x: Int): Int = x + 1
}
