// Auto-generated by GenerateInRangeExpressionTestData. Do not edit!
// WITH_STDLIB
// LANGUAGE: +RangeUntilOperator
@file:OptIn(ExperimentalStdlibApi::class)



val range0 = 1.0..<3.0
val range1 = 3.0..<1.0

val element0 = -1F
val element1 = -1.0
val element2 = 0F
val element3 = 0.0
val element4 = 1F
val element5 = 1.0
val element6 = 2F
val element7 = 2.0
val element8 = 3F
val element9 = 3.0
val element10 = 4F
val element11 = 4.0

fun box(): String {
    testR0xE0()
    testR0xE1()
    testR0xE2()
    testR0xE3()
    testR0xE4()
    testR0xE5()
    testR0xE6()
    testR0xE7()
    testR0xE8()
    testR0xE9()
    testR0xE10()
    testR0xE11()
    testR1xE0()
    testR1xE1()
    testR1xE2()
    testR1xE3()
    testR1xE4()
    testR1xE5()
    testR1xE6()
    testR1xE7()
    testR1xE8()
    testR1xE9()
    testR1xE10()
    testR1xE11()
    return "OK"
}

fun testR0xE0() {
    // with possible local optimizations
    if (-1F in 1.0..<3.0 != range0.contains(-1F)) throw AssertionError()
    if (-1F !in 1.0..<3.0 != !range0.contains(-1F)) throw AssertionError()
    if (!(-1F in 1.0..<3.0) != !range0.contains(-1F)) throw AssertionError()
    if (!(-1F !in 1.0..<3.0) != range0.contains(-1F)) throw AssertionError()
    // no local optimizations
    if (element0 in 1.0..<3.0 != range0.contains(element0)) throw AssertionError()
    if (element0 !in 1.0..<3.0 != !range0.contains(element0)) throw AssertionError()
    if (!(element0 in 1.0..<3.0) != !range0.contains(element0)) throw AssertionError()
    if (!(element0 !in 1.0..<3.0) != range0.contains(element0)) throw AssertionError()
}

fun testR0xE1() {
    // with possible local optimizations
    if (-1.0 in 1.0..<3.0 != range0.contains(-1.0)) throw AssertionError()
    if (-1.0 !in 1.0..<3.0 != !range0.contains(-1.0)) throw AssertionError()
    if (!(-1.0 in 1.0..<3.0) != !range0.contains(-1.0)) throw AssertionError()
    if (!(-1.0 !in 1.0..<3.0) != range0.contains(-1.0)) throw AssertionError()
    // no local optimizations
    if (element1 in 1.0..<3.0 != range0.contains(element1)) throw AssertionError()
    if (element1 !in 1.0..<3.0 != !range0.contains(element1)) throw AssertionError()
    if (!(element1 in 1.0..<3.0) != !range0.contains(element1)) throw AssertionError()
    if (!(element1 !in 1.0..<3.0) != range0.contains(element1)) throw AssertionError()
}

fun testR0xE2() {
    // with possible local optimizations
    if (0F in 1.0..<3.0 != range0.contains(0F)) throw AssertionError()
    if (0F !in 1.0..<3.0 != !range0.contains(0F)) throw AssertionError()
    if (!(0F in 1.0..<3.0) != !range0.contains(0F)) throw AssertionError()
    if (!(0F !in 1.0..<3.0) != range0.contains(0F)) throw AssertionError()
    // no local optimizations
    if (element2 in 1.0..<3.0 != range0.contains(element2)) throw AssertionError()
    if (element2 !in 1.0..<3.0 != !range0.contains(element2)) throw AssertionError()
    if (!(element2 in 1.0..<3.0) != !range0.contains(element2)) throw AssertionError()
    if (!(element2 !in 1.0..<3.0) != range0.contains(element2)) throw AssertionError()
}

fun testR0xE3() {
    // with possible local optimizations
    if (0.0 in 1.0..<3.0 != range0.contains(0.0)) throw AssertionError()
    if (0.0 !in 1.0..<3.0 != !range0.contains(0.0)) throw AssertionError()
    if (!(0.0 in 1.0..<3.0) != !range0.contains(0.0)) throw AssertionError()
    if (!(0.0 !in 1.0..<3.0) != range0.contains(0.0)) throw AssertionError()
    // no local optimizations
    if (element3 in 1.0..<3.0 != range0.contains(element3)) throw AssertionError()
    if (element3 !in 1.0..<3.0 != !range0.contains(element3)) throw AssertionError()
    if (!(element3 in 1.0..<3.0) != !range0.contains(element3)) throw AssertionError()
    if (!(element3 !in 1.0..<3.0) != range0.contains(element3)) throw AssertionError()
}

fun testR0xE4() {
    // with possible local optimizations
    if (1F in 1.0..<3.0 != range0.contains(1F)) throw AssertionError()
    if (1F !in 1.0..<3.0 != !range0.contains(1F)) throw AssertionError()
    if (!(1F in 1.0..<3.0) != !range0.contains(1F)) throw AssertionError()
    if (!(1F !in 1.0..<3.0) != range0.contains(1F)) throw AssertionError()
    // no local optimizations
    if (element4 in 1.0..<3.0 != range0.contains(element4)) throw AssertionError()
    if (element4 !in 1.0..<3.0 != !range0.contains(element4)) throw AssertionError()
    if (!(element4 in 1.0..<3.0) != !range0.contains(element4)) throw AssertionError()
    if (!(element4 !in 1.0..<3.0) != range0.contains(element4)) throw AssertionError()
}

fun testR0xE5() {
    // with possible local optimizations
    if (1.0 in 1.0..<3.0 != range0.contains(1.0)) throw AssertionError()
    if (1.0 !in 1.0..<3.0 != !range0.contains(1.0)) throw AssertionError()
    if (!(1.0 in 1.0..<3.0) != !range0.contains(1.0)) throw AssertionError()
    if (!(1.0 !in 1.0..<3.0) != range0.contains(1.0)) throw AssertionError()
    // no local optimizations
    if (element5 in 1.0..<3.0 != range0.contains(element5)) throw AssertionError()
    if (element5 !in 1.0..<3.0 != !range0.contains(element5)) throw AssertionError()
    if (!(element5 in 1.0..<3.0) != !range0.contains(element5)) throw AssertionError()
    if (!(element5 !in 1.0..<3.0) != range0.contains(element5)) throw AssertionError()
}

fun testR0xE6() {
    // with possible local optimizations
    if (2F in 1.0..<3.0 != range0.contains(2F)) throw AssertionError()
    if (2F !in 1.0..<3.0 != !range0.contains(2F)) throw AssertionError()
    if (!(2F in 1.0..<3.0) != !range0.contains(2F)) throw AssertionError()
    if (!(2F !in 1.0..<3.0) != range0.contains(2F)) throw AssertionError()
    // no local optimizations
    if (element6 in 1.0..<3.0 != range0.contains(element6)) throw AssertionError()
    if (element6 !in 1.0..<3.0 != !range0.contains(element6)) throw AssertionError()
    if (!(element6 in 1.0..<3.0) != !range0.contains(element6)) throw AssertionError()
    if (!(element6 !in 1.0..<3.0) != range0.contains(element6)) throw AssertionError()
}

fun testR0xE7() {
    // with possible local optimizations
    if (2.0 in 1.0..<3.0 != range0.contains(2.0)) throw AssertionError()
    if (2.0 !in 1.0..<3.0 != !range0.contains(2.0)) throw AssertionError()
    if (!(2.0 in 1.0..<3.0) != !range0.contains(2.0)) throw AssertionError()
    if (!(2.0 !in 1.0..<3.0) != range0.contains(2.0)) throw AssertionError()
    // no local optimizations
    if (element7 in 1.0..<3.0 != range0.contains(element7)) throw AssertionError()
    if (element7 !in 1.0..<3.0 != !range0.contains(element7)) throw AssertionError()
    if (!(element7 in 1.0..<3.0) != !range0.contains(element7)) throw AssertionError()
    if (!(element7 !in 1.0..<3.0) != range0.contains(element7)) throw AssertionError()
}

fun testR0xE8() {
    // with possible local optimizations
    if (3F in 1.0..<3.0 != range0.contains(3F)) throw AssertionError()
    if (3F !in 1.0..<3.0 != !range0.contains(3F)) throw AssertionError()
    if (!(3F in 1.0..<3.0) != !range0.contains(3F)) throw AssertionError()
    if (!(3F !in 1.0..<3.0) != range0.contains(3F)) throw AssertionError()
    // no local optimizations
    if (element8 in 1.0..<3.0 != range0.contains(element8)) throw AssertionError()
    if (element8 !in 1.0..<3.0 != !range0.contains(element8)) throw AssertionError()
    if (!(element8 in 1.0..<3.0) != !range0.contains(element8)) throw AssertionError()
    if (!(element8 !in 1.0..<3.0) != range0.contains(element8)) throw AssertionError()
}

fun testR0xE9() {
    // with possible local optimizations
    if (3.0 in 1.0..<3.0 != range0.contains(3.0)) throw AssertionError()
    if (3.0 !in 1.0..<3.0 != !range0.contains(3.0)) throw AssertionError()
    if (!(3.0 in 1.0..<3.0) != !range0.contains(3.0)) throw AssertionError()
    if (!(3.0 !in 1.0..<3.0) != range0.contains(3.0)) throw AssertionError()
    // no local optimizations
    if (element9 in 1.0..<3.0 != range0.contains(element9)) throw AssertionError()
    if (element9 !in 1.0..<3.0 != !range0.contains(element9)) throw AssertionError()
    if (!(element9 in 1.0..<3.0) != !range0.contains(element9)) throw AssertionError()
    if (!(element9 !in 1.0..<3.0) != range0.contains(element9)) throw AssertionError()
}

fun testR0xE10() {
    // with possible local optimizations
    if (4F in 1.0..<3.0 != range0.contains(4F)) throw AssertionError()
    if (4F !in 1.0..<3.0 != !range0.contains(4F)) throw AssertionError()
    if (!(4F in 1.0..<3.0) != !range0.contains(4F)) throw AssertionError()
    if (!(4F !in 1.0..<3.0) != range0.contains(4F)) throw AssertionError()
    // no local optimizations
    if (element10 in 1.0..<3.0 != range0.contains(element10)) throw AssertionError()
    if (element10 !in 1.0..<3.0 != !range0.contains(element10)) throw AssertionError()
    if (!(element10 in 1.0..<3.0) != !range0.contains(element10)) throw AssertionError()
    if (!(element10 !in 1.0..<3.0) != range0.contains(element10)) throw AssertionError()
}

fun testR0xE11() {
    // with possible local optimizations
    if (4.0 in 1.0..<3.0 != range0.contains(4.0)) throw AssertionError()
    if (4.0 !in 1.0..<3.0 != !range0.contains(4.0)) throw AssertionError()
    if (!(4.0 in 1.0..<3.0) != !range0.contains(4.0)) throw AssertionError()
    if (!(4.0 !in 1.0..<3.0) != range0.contains(4.0)) throw AssertionError()
    // no local optimizations
    if (element11 in 1.0..<3.0 != range0.contains(element11)) throw AssertionError()
    if (element11 !in 1.0..<3.0 != !range0.contains(element11)) throw AssertionError()
    if (!(element11 in 1.0..<3.0) != !range0.contains(element11)) throw AssertionError()
    if (!(element11 !in 1.0..<3.0) != range0.contains(element11)) throw AssertionError()
}

fun testR1xE0() {
    // with possible local optimizations
    if (-1F in 3.0..<1.0 != range1.contains(-1F)) throw AssertionError()
    if (-1F !in 3.0..<1.0 != !range1.contains(-1F)) throw AssertionError()
    if (!(-1F in 3.0..<1.0) != !range1.contains(-1F)) throw AssertionError()
    if (!(-1F !in 3.0..<1.0) != range1.contains(-1F)) throw AssertionError()
    // no local optimizations
    if (element0 in 3.0..<1.0 != range1.contains(element0)) throw AssertionError()
    if (element0 !in 3.0..<1.0 != !range1.contains(element0)) throw AssertionError()
    if (!(element0 in 3.0..<1.0) != !range1.contains(element0)) throw AssertionError()
    if (!(element0 !in 3.0..<1.0) != range1.contains(element0)) throw AssertionError()
}

fun testR1xE1() {
    // with possible local optimizations
    if (-1.0 in 3.0..<1.0 != range1.contains(-1.0)) throw AssertionError()
    if (-1.0 !in 3.0..<1.0 != !range1.contains(-1.0)) throw AssertionError()
    if (!(-1.0 in 3.0..<1.0) != !range1.contains(-1.0)) throw AssertionError()
    if (!(-1.0 !in 3.0..<1.0) != range1.contains(-1.0)) throw AssertionError()
    // no local optimizations
    if (element1 in 3.0..<1.0 != range1.contains(element1)) throw AssertionError()
    if (element1 !in 3.0..<1.0 != !range1.contains(element1)) throw AssertionError()
    if (!(element1 in 3.0..<1.0) != !range1.contains(element1)) throw AssertionError()
    if (!(element1 !in 3.0..<1.0) != range1.contains(element1)) throw AssertionError()
}

fun testR1xE2() {
    // with possible local optimizations
    if (0F in 3.0..<1.0 != range1.contains(0F)) throw AssertionError()
    if (0F !in 3.0..<1.0 != !range1.contains(0F)) throw AssertionError()
    if (!(0F in 3.0..<1.0) != !range1.contains(0F)) throw AssertionError()
    if (!(0F !in 3.0..<1.0) != range1.contains(0F)) throw AssertionError()
    // no local optimizations
    if (element2 in 3.0..<1.0 != range1.contains(element2)) throw AssertionError()
    if (element2 !in 3.0..<1.0 != !range1.contains(element2)) throw AssertionError()
    if (!(element2 in 3.0..<1.0) != !range1.contains(element2)) throw AssertionError()
    if (!(element2 !in 3.0..<1.0) != range1.contains(element2)) throw AssertionError()
}

fun testR1xE3() {
    // with possible local optimizations
    if (0.0 in 3.0..<1.0 != range1.contains(0.0)) throw AssertionError()
    if (0.0 !in 3.0..<1.0 != !range1.contains(0.0)) throw AssertionError()
    if (!(0.0 in 3.0..<1.0) != !range1.contains(0.0)) throw AssertionError()
    if (!(0.0 !in 3.0..<1.0) != range1.contains(0.0)) throw AssertionError()
    // no local optimizations
    if (element3 in 3.0..<1.0 != range1.contains(element3)) throw AssertionError()
    if (element3 !in 3.0..<1.0 != !range1.contains(element3)) throw AssertionError()
    if (!(element3 in 3.0..<1.0) != !range1.contains(element3)) throw AssertionError()
    if (!(element3 !in 3.0..<1.0) != range1.contains(element3)) throw AssertionError()
}

fun testR1xE4() {
    // with possible local optimizations
    if (1F in 3.0..<1.0 != range1.contains(1F)) throw AssertionError()
    if (1F !in 3.0..<1.0 != !range1.contains(1F)) throw AssertionError()
    if (!(1F in 3.0..<1.0) != !range1.contains(1F)) throw AssertionError()
    if (!(1F !in 3.0..<1.0) != range1.contains(1F)) throw AssertionError()
    // no local optimizations
    if (element4 in 3.0..<1.0 != range1.contains(element4)) throw AssertionError()
    if (element4 !in 3.0..<1.0 != !range1.contains(element4)) throw AssertionError()
    if (!(element4 in 3.0..<1.0) != !range1.contains(element4)) throw AssertionError()
    if (!(element4 !in 3.0..<1.0) != range1.contains(element4)) throw AssertionError()
}

fun testR1xE5() {
    // with possible local optimizations
    if (1.0 in 3.0..<1.0 != range1.contains(1.0)) throw AssertionError()
    if (1.0 !in 3.0..<1.0 != !range1.contains(1.0)) throw AssertionError()
    if (!(1.0 in 3.0..<1.0) != !range1.contains(1.0)) throw AssertionError()
    if (!(1.0 !in 3.0..<1.0) != range1.contains(1.0)) throw AssertionError()
    // no local optimizations
    if (element5 in 3.0..<1.0 != range1.contains(element5)) throw AssertionError()
    if (element5 !in 3.0..<1.0 != !range1.contains(element5)) throw AssertionError()
    if (!(element5 in 3.0..<1.0) != !range1.contains(element5)) throw AssertionError()
    if (!(element5 !in 3.0..<1.0) != range1.contains(element5)) throw AssertionError()
}

fun testR1xE6() {
    // with possible local optimizations
    if (2F in 3.0..<1.0 != range1.contains(2F)) throw AssertionError()
    if (2F !in 3.0..<1.0 != !range1.contains(2F)) throw AssertionError()
    if (!(2F in 3.0..<1.0) != !range1.contains(2F)) throw AssertionError()
    if (!(2F !in 3.0..<1.0) != range1.contains(2F)) throw AssertionError()
    // no local optimizations
    if (element6 in 3.0..<1.0 != range1.contains(element6)) throw AssertionError()
    if (element6 !in 3.0..<1.0 != !range1.contains(element6)) throw AssertionError()
    if (!(element6 in 3.0..<1.0) != !range1.contains(element6)) throw AssertionError()
    if (!(element6 !in 3.0..<1.0) != range1.contains(element6)) throw AssertionError()
}

fun testR1xE7() {
    // with possible local optimizations
    if (2.0 in 3.0..<1.0 != range1.contains(2.0)) throw AssertionError()
    if (2.0 !in 3.0..<1.0 != !range1.contains(2.0)) throw AssertionError()
    if (!(2.0 in 3.0..<1.0) != !range1.contains(2.0)) throw AssertionError()
    if (!(2.0 !in 3.0..<1.0) != range1.contains(2.0)) throw AssertionError()
    // no local optimizations
    if (element7 in 3.0..<1.0 != range1.contains(element7)) throw AssertionError()
    if (element7 !in 3.0..<1.0 != !range1.contains(element7)) throw AssertionError()
    if (!(element7 in 3.0..<1.0) != !range1.contains(element7)) throw AssertionError()
    if (!(element7 !in 3.0..<1.0) != range1.contains(element7)) throw AssertionError()
}

fun testR1xE8() {
    // with possible local optimizations
    if (3F in 3.0..<1.0 != range1.contains(3F)) throw AssertionError()
    if (3F !in 3.0..<1.0 != !range1.contains(3F)) throw AssertionError()
    if (!(3F in 3.0..<1.0) != !range1.contains(3F)) throw AssertionError()
    if (!(3F !in 3.0..<1.0) != range1.contains(3F)) throw AssertionError()
    // no local optimizations
    if (element8 in 3.0..<1.0 != range1.contains(element8)) throw AssertionError()
    if (element8 !in 3.0..<1.0 != !range1.contains(element8)) throw AssertionError()
    if (!(element8 in 3.0..<1.0) != !range1.contains(element8)) throw AssertionError()
    if (!(element8 !in 3.0..<1.0) != range1.contains(element8)) throw AssertionError()
}

fun testR1xE9() {
    // with possible local optimizations
    if (3.0 in 3.0..<1.0 != range1.contains(3.0)) throw AssertionError()
    if (3.0 !in 3.0..<1.0 != !range1.contains(3.0)) throw AssertionError()
    if (!(3.0 in 3.0..<1.0) != !range1.contains(3.0)) throw AssertionError()
    if (!(3.0 !in 3.0..<1.0) != range1.contains(3.0)) throw AssertionError()
    // no local optimizations
    if (element9 in 3.0..<1.0 != range1.contains(element9)) throw AssertionError()
    if (element9 !in 3.0..<1.0 != !range1.contains(element9)) throw AssertionError()
    if (!(element9 in 3.0..<1.0) != !range1.contains(element9)) throw AssertionError()
    if (!(element9 !in 3.0..<1.0) != range1.contains(element9)) throw AssertionError()
}

fun testR1xE10() {
    // with possible local optimizations
    if (4F in 3.0..<1.0 != range1.contains(4F)) throw AssertionError()
    if (4F !in 3.0..<1.0 != !range1.contains(4F)) throw AssertionError()
    if (!(4F in 3.0..<1.0) != !range1.contains(4F)) throw AssertionError()
    if (!(4F !in 3.0..<1.0) != range1.contains(4F)) throw AssertionError()
    // no local optimizations
    if (element10 in 3.0..<1.0 != range1.contains(element10)) throw AssertionError()
    if (element10 !in 3.0..<1.0 != !range1.contains(element10)) throw AssertionError()
    if (!(element10 in 3.0..<1.0) != !range1.contains(element10)) throw AssertionError()
    if (!(element10 !in 3.0..<1.0) != range1.contains(element10)) throw AssertionError()
}

fun testR1xE11() {
    // with possible local optimizations
    if (4.0 in 3.0..<1.0 != range1.contains(4.0)) throw AssertionError()
    if (4.0 !in 3.0..<1.0 != !range1.contains(4.0)) throw AssertionError()
    if (!(4.0 in 3.0..<1.0) != !range1.contains(4.0)) throw AssertionError()
    if (!(4.0 !in 3.0..<1.0) != range1.contains(4.0)) throw AssertionError()
    // no local optimizations
    if (element11 in 3.0..<1.0 != range1.contains(element11)) throw AssertionError()
    if (element11 !in 3.0..<1.0 != !range1.contains(element11)) throw AssertionError()
    if (!(element11 in 3.0..<1.0) != !range1.contains(element11)) throw AssertionError()
    if (!(element11 !in 3.0..<1.0) != range1.contains(element11)) throw AssertionError()
}


