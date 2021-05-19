plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.jmh")
    id("org.checkerframework") version "0.5.17"
}

description = "A set of generic services and utilities."

//gradlebuildJava.usedInWorkers()

moduleIdentity.createBuildReceipt()

apply(plugin = "org.checkerframework")

repositories {
    mavenCentral()
}

dependencies {
    api(project(":base-annotations"))
    api(project(":hashing"))
    api(project(":build-operations"))

    implementation(libs.asm)
    implementation(libs.commonsIo)
    implementation(libs.commonsLang)
    implementation(libs.guava)
    implementation(libs.inject)
    implementation(libs.slf4jApi)

    integTestImplementation(project(":logging"))

    testFixturesImplementation(libs.guava)
    testImplementation(testFixtures(project(":core")))

    integTestDistributionRuntimeOnly(project(":distributions-core"))

    jmh(platform(project(":distributions-dependencies")))
    jmh(libs.bouncycastleProvider)
    jmh(libs.guava)

    checkerFramework("net.sridharan.objectconstruction:object-construction-checker:0.1.12")
    implementation("net.sridharan.objectconstruction:object-construction-qual:0.1.12")
}

classycle {
    // Needed for the factory methods in the base class
    excludePatterns.add("org/gradle/util/GradleVersion**")
}

configure<org.checkerframework.gradle.plugin.CheckerFrameworkExtension> {
    checkers.add("org.checkerframework.checker.objectconstruction.ObjectConstructionChecker")
    extraJavacArgs.add("-AsuppressWarnings=type.anno.before")
    extraJavacArgs.add("-Werror")
    extraJavacArgs.add("-AcheckMustCall")
    extraJavacArgs.add("-AwarnUnneededSuppressions")
}

afterEvaluate {
    tasks.withType(JavaCompile::class) {
        options.compilerArgs.add("-Xlint:-processing")
    }
}

jmh.includes.set(listOf("HashingAlgorithmsBenchmark"))
