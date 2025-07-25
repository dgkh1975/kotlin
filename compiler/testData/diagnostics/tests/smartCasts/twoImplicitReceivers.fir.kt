// RUN_PIPELINE_TILL: BACKEND
interface IA
interface IB

object A : IA {
    fun B.foo() {  }
}

object B : IB

fun test(a: IA, b: IB) {
    with(a) lambda1@{
        with(b) lambda2@{
            if (this@lambda1 is A && this@lambda2 is B) {
                foo()
            }
        }
    }
}

/* GENERATED_FIR_TAGS: andExpression, funWithExtensionReceiver, functionDeclaration, ifExpression, interfaceDeclaration,
isExpression, lambdaLiteral, objectDeclaration, smartcast, thisExpression */
