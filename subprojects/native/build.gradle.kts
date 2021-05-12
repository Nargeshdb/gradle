plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.jmh")
    id("org.checkerframework") version "0.5.17"
}

description = "This project contains various native operating system integration utilities"

//gradlebuildJava.usedInWorkers()

apply(plugin = "org.checkerframework")

repositories {
    mavenCentral()
}

dependencies {
    api(project(":files"))

    implementation(project(":base-services"))
    implementation(project(":file-temp"))

    implementation(libs.nativePlatform)
    implementation(libs.nativePlatformFileEvents)
    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.commonsIo)
    implementation(libs.jansi)
    implementation(libs.inject)

    testImplementation(testFixtures(project(":core")))
    testImplementation(testFixtures(project(":logging")))

    jmhImplementation(project(":files"))

    checkerFramework("net.sridharan.objectconstruction:object-construction-checker:0.1.12")
    implementation("net.sridharan.objectconstruction:object-construction-qual:0.1.12")
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

jmh {
    fork = 1
    threads = 2
    warmupIterations = 10
    synchronizeIterations = false
}
