plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.jmh")
    id("org.checkerframework") version "0.5.17"
}

apply(plugin = "org.checkerframework")

repositories {
    mavenCentral()
}

dependencies {
    checkerFramework("net.sridharan.objectconstruction:object-construction-checker:0.1.12")
    implementation("net.sridharan.objectconstruction:object-construction-qual:0.1.12")
    api(project(":build-cache-base"))
    api(project(":snapshots"))

    implementation(project(":base-services"))
    implementation(project(":core-api"))
    implementation(project(":files"))
    implementation(project(":file-temp"))
    implementation(project(":native"))
    implementation(project(":persistent-cache"))
    implementation(project(":resources"))
    implementation(project(":logging"))

    implementation(libs.slf4jApi)
    implementation(libs.guava)
    implementation(libs.inject)

    jmhImplementation(platform(project(":distributions-dependencies")))
    jmhImplementation(libs.ant)
    jmhImplementation(libs.commonsCompress)
    jmhImplementation(libs.aircompressor)
    jmhImplementation(libs.snappy)
    jmhImplementation(libs.jtar)
    jmhImplementation(libs.commonsIo)

    testImplementation(project(":model-core"))
    testImplementation(project(":file-collections"))
    testImplementation(testFixtures(project(":core")))
    testImplementation(testFixtures(project(":base-services")))

    integTestDistributionRuntimeOnly(project(":distributions-core"))
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
