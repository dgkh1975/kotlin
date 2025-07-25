
plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    testApi(kotlinStdlib("jdk8"))
    testApi(project(":kotlin-scripting-compiler"))
    testApi(project(":core:descriptors"))
    testApi(project(":core:descriptors.jvm"))
    testApi(project(":core:deserialization"))
    testApi(project(":compiler:util"))
    testApi(project(":compiler:tests-mutes"))
    testApi(project(":compiler:backend"))
    testApi(project(":compiler:frontend"))
    testApi(project(":compiler:frontend.java"))
    testApi(project(":compiler:util"))
    testApi(project(":compiler:psi:psi-api"))
    testApi(project(":compiler:cli-common"))
    testApi(project(":compiler:cli"))
    testApi(project(":compiler:cli-js"))
    testApi(project(":compiler:serialization"))
    testApi(project(":compiler:fir:entrypoint"))
    testApi(project(":compiler:fir:fir2ir:jvm-backend"))
    testApi(project(":compiler:backend.jvm.entrypoint"))
    testApi(projectTests(":compiler:test-infrastructure-utils"))
    testApi(project(":kotlin-preloader"))
    testApi(commonDependency("com.android.tools:r8"))
    testCompileOnly(intellijCore())

    testApi(libs.guava)
    testApi(libs.intellij.asm)
    testApi(commonDependency("org.jetbrains.intellij.deps:log4j"))
    testApi(intellijJDom())
}

optInToUnsafeDuringIrConstructionAPI()
optInToK1Deprecation()

sourceSets {
    "main" { none() }
    "test" { projectDefault() }
}

testsJar {}
