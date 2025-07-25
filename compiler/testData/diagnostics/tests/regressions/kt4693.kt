// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
enum class MyEnum {
    K;
    
    inline fun doSmth(f: (MyEnum) -> String) : String {
        // This function should be inline
        return f(K)
    }
}

/* GENERATED_FIR_TAGS: enumDeclaration, enumEntry, functionDeclaration, functionalType, inline */
