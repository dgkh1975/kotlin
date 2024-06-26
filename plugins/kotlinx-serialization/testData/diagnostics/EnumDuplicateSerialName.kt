// FIR_IDENTICAL
// WITH_STDLIB
// SKIP_TXT
import kotlinx.serialization.*


@Serializable
enum class ImplicitlyDuplicated {
    @SerialName("foo")
    FIRST,
    <!DUPLICATE_SERIAL_NAME_ENUM!>@SerialName("foo")<!>
    SECOND
}

@Serializable
enum class ExplicitlyDuplicated {
    FIRST,
    SECOND,
    <!DUPLICATE_SERIAL_NAME_ENUM!>@SerialName("FIRST")<!>
    THIRD
}

@Serializable
enum class ReversedExplicitlyDuplicated {
    <!DUPLICATE_SERIAL_NAME_ENUM!>@SerialName("THIRD")<!>
    FIRST,
    SECOND,
    THIRD
}

const val X = "THI"
const val Y = "RD"

@Serializable
enum class WithConstants {
    <!DUPLICATE_SERIAL_NAME_ENUM!>@SerialName(X + Y)<!>
    FIRST,
    SECOND,
    THIRD
}

