// RUN_PIPELINE_TILL: FRONTEND
<!WRONG_MODIFIER_TARGET!>enum<!> interface Some {
    // Enum part
    D;

    // Interface like part
    fun test()
    val foo: Int
}

/* GENERATED_FIR_TAGS: enumEntry, functionDeclaration, interfaceDeclaration, propertyDeclaration */
