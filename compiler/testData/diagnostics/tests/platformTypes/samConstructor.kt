// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// FILE: p/J.java

package p;

public class J {
    public static void c(java.util.Comparator<Integer> c) {}

}

// FILE: k.kt

import java.util.*
import p.*

fun test() {
    J.c(Comparator { a, b -> b - a })
}

/* GENERATED_FIR_TAGS: additiveExpression, flexibleType, functionDeclaration, javaFunction, lambdaLiteral */
