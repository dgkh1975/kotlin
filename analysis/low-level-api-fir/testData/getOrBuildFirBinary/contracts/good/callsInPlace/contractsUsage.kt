// DECLARATION_TYPE: org.jetbrains.kotlin.psi.KtClass
// MAIN_FILE_NAME: Foo
import kotlin.contracts.*

class Foo {
    @OptIn(ExperimentalContracts::class)
    fun bar(x: () -> Unit) {
        contract {
            callsInPlace(x, InvocationKind.EXACTLY_ONCE)
        }

        x.invoke()
    }

    @OptIn(ExperimentalContracts::class)
    fun (() -> Unit).baz() {
        contract {
            callsInPlace(this@baz, InvocationKind.AT_MOST_ONCE)
        }

        if (true) {
            this.invoke()
        }
    }

    @OptIn(ExperimentalContracts::class)
    fun foo(x: () -> Unit, y: () -> Unit) {
        contract {
            callsInPlace(x, InvocationKind.AT_LEAST_ONCE)
            callsInPlace(y, InvocationKind.AT_MOST_ONCE)
        }

        if (true) {
            x.invoke()
            y.baz()
            return
        }

        bar(x)
    }
}