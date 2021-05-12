plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.publish-public-libraries")
    id("org.checkerframework") version "0.5.17"
}

description = "Base tools to work with files"

//gradlebuildJava.usedInWorkers()

apply(plugin = "org.checkerframework")

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":base-annotations"))
    implementation(libs.guava)
    implementation(libs.slf4jApi)

    testImplementation(project(":native"))
    testImplementation(project(":base-services")) {
        because("TextUtil is needed")
    }
    testImplementation(testFixtures(project(":native")))

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
