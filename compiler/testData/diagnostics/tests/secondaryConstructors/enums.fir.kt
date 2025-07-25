// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_PARAMETER
enum class A {
    W(1), X(1, 2), Y(3.0), Z(""), E();

    constructor()
    constructor(x: Int)
    constructor(x: Int, y: Int): this(x+y)
    constructor(x: Double): this(x.toInt(), 1)
    constructor(x: String): <!DELEGATION_SUPER_CALL_IN_ENUM_CONSTRUCTOR!>super<!>(x, 1)
}

enum class B(x: Int) {
    W(1), X(1, 2), Y(3.0), Z("");

    constructor(x: Int, y: Int): this(x+y)
    constructor(x: Double): this(x.toInt(), 1)
    constructor(x: String): <!DELEGATION_SUPER_CALL_IN_ENUM_CONSTRUCTOR, PRIMARY_CONSTRUCTOR_DELEGATION_CALL_EXPECTED!>super<!>(x, 1)
}

enum class C {
    EMPTY(); // may be we should avoid explicit call here
    constructor()
}

enum class D(val prop: Int) {
    X(123) {
        override fun f() = 1
    },
    Y() {
        override fun f() = prop
    },
    Z("abc") {
        override fun f() = prop
    };

    constructor(): this(1)
    constructor(x: String): this(x.length)

    abstract fun f(): Int
}

/* GENERATED_FIR_TAGS: additiveExpression, enumDeclaration, enumEntry, functionDeclaration, integerLiteral,
primaryConstructor, propertyDeclaration, secondaryConstructor */
