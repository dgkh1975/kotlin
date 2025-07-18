// RUN_PIPELINE_TILL: FRONTEND
// FILE: K1.kt
class KSub : J1()

fun main(k: KSub, vString: SuperClass<String>.NestedInSuperClass, vInt: SuperClass<Int>.NestedInSuperClass) {
    k.getImpl().nestedI(vString)

    // TODO: Support parametrisized inner classes
    k.getImpl().nestedI(<!ARGUMENT_TYPE_MISMATCH!>vInt<!>)
    k.getNestedSubClass().nested("")
    k.getNestedSubClass().nested(<!ARGUMENT_TYPE_MISMATCH!>1<!>)
}

// FILE: J1.java
public class J1 extends KFirst {

    public class NestedSubClass extends NestedInSuperClass {}
    public abstract class NestedIImpl implements NestedInI<NestedInSuperClass> {}

    public NestedIImpl getImpl() { return null; }
    public NestedSubClass getNestedSubClass() { return null; }
}

// FILE: K2.kt
open class KFirst : SuperClass<String>(), SuperI<Int>

// FILE: K3.kt
abstract class SuperClass<T> {
    inner open class NestedInSuperClass {
        fun nested(x: T) {}
    }
}

interface SuperI<E> {
    interface NestedInI<F> {
        fun nestedI(f: F) {}
    }
}

/* GENERATED_FIR_TAGS: classDeclaration, flexibleType, functionDeclaration, inner, integerLiteral, interfaceDeclaration,
javaFunction, javaType, nestedClass, nullableType, stringLiteral, typeParameter */
