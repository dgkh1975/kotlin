// RUN_PIPELINE_TILL: FIR2IR
// WITH_KOTLIN_JVM_ANNOTATIONS
// LANGUAGE:+DirectJavaActualization
// MODULE: m1-common
// FILE: common.kt

expect annotation class Foo

// MODULE: m2-jvm()()(m1-common)
// FILE: Foo.java
@kotlin.RequiresOptIn
@kotlin.annotations.jvm.KotlinActual
public @interface Foo {}

/* GENERATED_FIR_TAGS: annotationDeclaration, expect */
