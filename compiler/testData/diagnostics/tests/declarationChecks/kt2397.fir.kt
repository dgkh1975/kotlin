// RUN_PIPELINE_TILL: FRONTEND
//KT-2397 Prohibit final methods in traits with no implementation
package a

interface T {
    <!WRONG_MODIFIER_CONTAINING_DECLARATION!>final<!> fun foo()
    <!WRONG_MODIFIER_CONTAINING_DECLARATION!>final<!> val b : Int

    <!WRONG_MODIFIER_CONTAINING_DECLARATION!>final<!> fun bar() {}
    <!WRONG_MODIFIER_CONTAINING_DECLARATION!>final<!> val c : Int
       get() = 42

    <!WRONG_MODIFIER_CONTAINING_DECLARATION!>final<!> val d = <!PROPERTY_INITIALIZER_IN_INTERFACE!>1<!>
}

class A {
    final <!NON_ABSTRACT_FUNCTION_WITH_NO_BODY!>fun foo()<!>
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, getter, integerLiteral, interfaceDeclaration,
propertyDeclaration */
