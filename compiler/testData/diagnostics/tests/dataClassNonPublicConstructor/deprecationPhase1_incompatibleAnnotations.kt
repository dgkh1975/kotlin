// RUN_PIPELINE_TILL: FRONTEND
// WITH_STDLIB
// LANGUAGE: -ErrorAboutDataClassCopyVisibilityChange, -DataClassCopyRespectsConstructorVisibility
@kotlin.ConsistentCopyVisibility
@kotlin.ExposedCopyVisibility
data class Data private constructor(val x: Int)

/* GENERATED_FIR_TAGS: classDeclaration, data, primaryConstructor, propertyDeclaration */
