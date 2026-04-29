declare namespace JS_TESTS {
    type Nullable<T> = T | null | undefined
    function KtSingleton<T>(): T & (abstract new() => any);

    namespace foo {
        function withIntroducedAt(x: number, y?: number, o1?: string, k?: string, o2?: string): string;
        function nonAscendingVersion(y?: number, o?: string, k?: string): void;
        function invalidParameterPosition(x: number | undefined, y: number | undefined, z: number): void;
        function box(): string;
        class Simple /* implements kotlin.Annotation */ {
            constructor();
        }
        namespace Simple {
            /** @deprecated $metadata$ is used for internal purposes, please don't use it in your code, because it can be removed at any moment */
            namespace $metadata$ {
                const constructor: abstract new () => Simple;
            }
        }
        class WithStringParam /* implements kotlin.Annotation */ {
            constructor(message: string);
            get message(): string;
        }
        namespace WithStringParam {
            /** @deprecated $metadata$ is used for internal purposes, please don't use it in your code, because it can be removed at any moment */
            namespace $metadata$ {
                const constructor: abstract new () => WithStringParam;
            }
        }
        class WithMultipleParams /* implements kotlin.Annotation */ {
            constructor(name: string, count: number);
            get name(): string;
            get count(): number;
        }
        namespace WithMultipleParams {
            /** @deprecated $metadata$ is used for internal purposes, please don't use it in your code, because it can be removed at any moment */
            namespace $metadata$ {
                const constructor: abstract new () => WithMultipleParams;
            }
        }
        class WithDefaultValue /* implements kotlin.Annotation */ {
            constructor(level?: number);
            get level(): number;
        }
        namespace WithDefaultValue {
            /** @deprecated $metadata$ is used for internal purposes, please don't use it in your code, because it can be removed at any moment */
            namespace $metadata$ {
                const constructor: abstract new () => WithDefaultValue;
            }
        }
        class WithBooleanParam /* implements kotlin.Annotation */ {
            constructor(enabled: boolean);
            get enabled(): boolean;
        }
        namespace WithBooleanParam {
            /** @deprecated $metadata$ is used for internal purposes, please don't use it in your code, because it can be removed at any moment */
            namespace $metadata$ {
                const constructor: abstract new () => WithBooleanParam;
            }
        }
    }
}
