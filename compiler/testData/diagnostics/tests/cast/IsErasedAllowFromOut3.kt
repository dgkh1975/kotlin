// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
open class Base
class Derived<E : CharSequence> : Base()
fun f(entry: Base) = entry is Derived<out CharSequence>

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, isExpression, outProjection, typeConstraint, typeParameter */
