// !WITH_NEW_INFERENCE
package conflictingSubstitutions
//+JDK

import java.util.*

fun <R> elemAndList(r: R, t: MutableList<R>): R = r
fun <R> R.elemAndListWithReceiver(r: R, t: MutableList<R>): R = r

fun test() {
    val s = <!TYPE_INFERENCE_CONFLICTING_SUBSTITUTIONS{OI}!>elemAndList<!>(11, list("72"))

    val u = 11.<!TYPE_INFERENCE_CONFLICTING_SUBSTITUTIONS{OI}!>elemAndListWithReceiver<!>(4, list("7"))
}

fun <T> list(value: T) : ArrayList<T> {
    val list = ArrayList<T>()
    list.add(value)
    return list
}
