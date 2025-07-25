// RUN_PIPELINE_TILL: FIR2IR
// WITH_KOTLIN_JVM_ANNOTATIONS
// LANGUAGE:+DirectJavaActualization
// WITH_STDLIB

// MODULE: m1-common
// FILE: common.kt
<!JAVA_DIRECT_ACTUALIZATION_DEFAULT_PARAMETERS_IN_ACTUAL_FUNCTION{JVM}!>expect class Foo {
}<!>

// MODULE: m2-jvm()()(m1-common)
// FILE: Foo.java
@kotlin.annotations.jvm.KotlinActual public class Foo extends Base {
}

// FILE: jvm.kt
open class Base {
    fun foo(a: Int = 1) {}
}

/* GENERATED_FIR_TAGS: classDeclaration, expect, functionDeclaration, integerLiteral */
