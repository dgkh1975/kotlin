// RUN_PIPELINE_TILL: BACKEND
/*
 * KOTLIN DIAGNOSTICS SPEC TEST (POSITIVE)
 *
 * SPEC VERSION: 0.1-313
 * PRIMARY LINKS: expressions, when-expression -> paragraph 5 -> sentence 1
 * expressions, when-expression, exhaustive-when-expressions -> paragraph 2 -> sentence 9
 * expressions, when-expression, exhaustive-when-expressions -> paragraph 2 -> sentence 10
 * control--and-data-flow-analysis, performing-analysis-on-the-control-flow-graph, variable-initialization-analysis -> paragraph 2 -> sentence 1
 * control--and-data-flow-analysis, performing-analysis-on-the-control-flow-graph, variable-initialization-analysis -> paragraph 2 -> sentence 3
 */

enum class MyEnum {
    A, B
}

fun foo(x: MyEnum?): Int {
    val y: Int
    // See KT-6046: y is always initialized
    when (x) {
        MyEnum.A -> y = 1
        MyEnum.B -> y = 2
        null -> y = 0
    }
    return y
}

/* GENERATED_FIR_TAGS: assignment, enumDeclaration, enumEntry, equalityExpression, functionDeclaration, integerLiteral,
localProperty, nullableType, propertyDeclaration, smartcast, whenExpression, whenWithSubject */
