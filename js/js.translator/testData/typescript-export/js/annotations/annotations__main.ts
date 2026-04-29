import withIntroducedAt = JS_TESTS.foo.withIntroducedAt;

function assert(condition: boolean) {
    if (!condition) {
        throw "Assertion failed";
    }
}

function box(): string {
    assert(withIntroducedAt(42) == "OK42");

    return "OK";
}
