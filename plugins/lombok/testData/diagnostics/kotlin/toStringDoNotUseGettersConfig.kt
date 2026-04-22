// FULL_JDK

// FILE: test.kt

import lombok.ToString

// Don't report warning on `doNotUseGetters` in config because it's still relevant for Java code
@ToString
class Example(val x: Int)

// FILE: lombok.config
lombok.toString.doNotUseGetters=true
