// LATEST_LV_DIFFERENCE
// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-66392
// FILE: Java1.java
public class Java1 extends KotlinClass  {
    @Override
    public String getE() {
        return "2";
    }
}

// FILE: test.kt
// The order of Kotlin classes is important
class B : Java1() {
    override <!ACCIDENTAL_OVERRIDE!>var e<!> = super.e
}

open class KotlinClass {
    open var e = "1"
}

/* GENERATED_FIR_TAGS: classDeclaration, javaType, override, propertyDeclaration, stringLiteral, superExpression */
