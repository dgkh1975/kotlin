// IGNORE_FIR_DIAGNOSTICS
// RUN_PIPELINE_TILL: BACKEND
// MODULE: m1-common
// FILE: common.kt

expect class Foo {
    actual fun <!ACTUAL_WITHOUT_EXPECT, ACTUAL_WITHOUT_EXPECT{METADATA}!>bar<!>()
}

// MODULE: m1-jvm()()(m1-common)
// FILE: jvm.kt

actual class Foo {
    actual fun bar() {}
}

/* GENERATED_FIR_TAGS: actual, classDeclaration, expect, functionDeclaration */
