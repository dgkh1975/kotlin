// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-46120, KT-72140
// LANGUAGE: -ForbidImplementationByDelegationWithDifferentGenericSignature

// FILE: JI.java

public interface JI {
    <C> C foo();
}

// FILE: JC.java

public class JC implements JI {
    @Override
    public String foo() {
        return null;
    }
}

// FILE: KI.kt

interface KI {
    fun <T> foo(): T
}

// FILE: JKC.java

public class JKC implements KI {
    @Override
    public String foo() {
        return null;
    }
}

// FILE: test.kt

class C: <!IMPLEMENTATION_BY_DELEGATION_WITH_DIFFERENT_GENERIC_SIGNATURE_WARNING!>JI<!> by JC(), KI

class C2: <!IMPLEMENTATION_BY_DELEGATION_WITH_DIFFERENT_GENERIC_SIGNATURE_WARNING!>KI<!> by JKC(), JI

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, inheritanceDelegation, interfaceDeclaration, javaFunction,
javaType, nullableType, typeParameter */
