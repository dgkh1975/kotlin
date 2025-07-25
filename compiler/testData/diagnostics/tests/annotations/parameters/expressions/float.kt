// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
package test


annotation class Ann(
        val d1: Float,
        val d2: Float,
        val d3: Double
)

@Ann(1.toFloat() + 1.toFloat(), 1.toFloat() + 1, 1.toFloat() + 1.0) class MyClass

// EXPECTED: @Ann(d1 = 2.0.toFloat(), d2 = 2.0.toFloat(), d3 = 2.0.toDouble())

/* GENERATED_FIR_TAGS: additiveExpression, annotationDeclaration, classDeclaration, integerLiteral, primaryConstructor,
propertyDeclaration */
