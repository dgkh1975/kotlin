// RUN_PIPELINE_TILL: FRONTEND
// FIR_IDENTICAL
// FILE: base/Base.java
package base;

public abstract class Base {
    public void foo() {
        packagePrivateFoo();
    }

    /* package-private */ abstract void packagePrivateFoo();
}

// FILE: Impl.kt
package impl
import base.*

<!INVISIBLE_ABSTRACT_MEMBER_FROM_SUPER_ERROR!>class Impl<!> : Base()

fun foo() {
    Impl().foo()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, javaFunction, javaType */
