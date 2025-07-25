// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE
// accidentally reported (K1 only) on x.addFirst/addLast/removeFirst/removeLast
// ISSUE: KT-68193
// ISSUE: KT-67804

abstract class A1<E1> : MutableList<E1> {
    override fun addFirst(element: E1) {}
    override fun addLast(element: E1) {}

    override fun removeFirst(): E1 = super.removeFirst()
    override fun removeLast(): E1 = super.removeLast()
}

abstract class A2<E2> : MutableList<E2> {
    <!NOTHING_TO_OVERRIDE!>override<!> fun addFirst(element: E2?) {}
    <!NOTHING_TO_OVERRIDE!>override<!> fun addLast(element: E2?) {}

    override fun removeFirst(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>E2?<!> = super.removeFirst()
    override fun removeLast(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>E2?<!> = super.removeLast()
}

abstract class A3 : MutableList<String> {
    override fun addFirst(element: String) {}
    override fun addLast(element: String) {}

    override fun removeFirst(): String = super.removeFirst()
    override fun removeLast(): String = super.removeLast()
}

abstract class A4 : MutableList<String> {
    <!NOTHING_TO_OVERRIDE!>override<!> fun addFirst(element: String?) {}
    <!NOTHING_TO_OVERRIDE!>override<!> fun addLast(element: String?) {}

    override fun removeFirst(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>String?<!> = super.removeFirst()
    override fun removeLast(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>String?<!> = super.removeLast()
}

abstract class A5<E5> : ArrayList<E5>() {
    override fun addFirst(element: E5) {}
    override fun addLast(element: E5) {}

    override fun removeFirst(): E5 = super.removeFirst()
    override fun removeLast(): E5 = super.removeLast()
}

abstract class A6<E6> : ArrayList<E6>()  {
    <!NOTHING_TO_OVERRIDE!>override<!> fun addFirst(element: E6?) {}
    <!NOTHING_TO_OVERRIDE!>override<!> fun addLast(element: E6?) {}

    override fun removeFirst(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>E6?<!> = super.removeFirst()
    override fun removeLast(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>E6?<!> = super.removeLast()
}

abstract class A7 : ArrayList<String>() {
    override fun addFirst(element: String) {}
    override fun addLast(element: String) {}

    override fun removeFirst(): String = super.removeFirst()
    override fun removeLast(): String = super.removeLast()
}

abstract class A8 : ArrayList<String>() {
    <!NOTHING_TO_OVERRIDE!>override<!> fun addFirst(element: String?) {}
    <!NOTHING_TO_OVERRIDE!>override<!> fun addLast(element: String?) {}

    override fun removeFirst(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>String?<!> = super.removeFirst()
    override fun removeLast(): <!RETURN_TYPE_MISMATCH_ON_OVERRIDE!>String?<!> = super.removeLast()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, nullableType, override, superExpression, typeParameter */
