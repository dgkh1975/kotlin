// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// CHECK_TYPE
//KT-4420 Type inference with type projections

class Foo<T>
fun <T> Foo<T>.bar(): T = throw Exception()

fun main() {
    val f: Foo<out String> = Foo()
    f.bar() checkType { _<String>() }
}

/* GENERATED_FIR_TAGS: capturedType, classDeclaration, funWithExtensionReceiver, functionDeclaration, functionalType,
infix, lambdaLiteral, localProperty, nullableType, outProjection, propertyDeclaration, typeParameter, typeWithExtension */
