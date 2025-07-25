// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// LANGUAGE: +ProhibitDataClassesOverridingCopy

interface WithCopy<T> {
    fun copy(str: T): WithCopy<T>
}

data class Test(val str: String, val int: Int) : WithCopy<String> {
    override fun copy(str: String) = copy(str, int)
}

/* GENERATED_FIR_TAGS: classDeclaration, data, functionDeclaration, interfaceDeclaration, nullableType, override,
primaryConstructor, propertyDeclaration, typeParameter */
