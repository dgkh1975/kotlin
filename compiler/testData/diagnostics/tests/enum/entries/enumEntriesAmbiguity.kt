// RUN_PIPELINE_TILL: BACKEND
// LANGUAGE: +EnumEntries -PrioritizedEnumEntries -ForbidEnumEntryNamedEntries
// WITH_STDLIB
// FIR_DUMP

enum class Ambiguous {
    first, <!DEPRECATED_DECLARATION_OF_ENUM_ENTRY!>entries;<!>
}

val e = Ambiguous.entries.ordinal

/* GENERATED_FIR_TAGS: enumDeclaration, enumEntry, propertyDeclaration */
