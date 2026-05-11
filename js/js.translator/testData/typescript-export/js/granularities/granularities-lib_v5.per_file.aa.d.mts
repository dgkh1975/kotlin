// FILE: kotlin_lib1/lib1a.kt.d.mts
type Nullable<T> = T | null | undefined
declare function KtSingleton<T>(): T & (abstract new() => any);
export declare function foo(): any/* List<number> */;

// FILE: kotlin_lib1/lib1b.kt.d.mts
type Nullable<T> = T | null | undefined
declare function KtSingleton<T>(): T & (abstract new() => any);
export declare function bar(): any/* List<number> */;

// FILE: kotlin_lib2/lib2.kt.d.mts
type Nullable<T> = T | null | undefined
declare function KtSingleton<T>(): T & (abstract new() => any);
export declare function baz(): any/* List<number> */;

// FILE: kotlin_main/main.kt.d.mts
type Nullable<T> = T | null | undefined
declare function KtSingleton<T>(): T & (abstract new() => any);
export declare function box(): string;

