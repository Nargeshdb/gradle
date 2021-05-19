plugins {
    id("gradlebuild.distribution.api-java")
    id("gradlebuild.publish-public-libraries")
    id("org.checkerframework") version "0.5.17"
}

description = "API extraction for Java"

apply(plugin = "org.checkerframework")

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":base-annotations"))
    implementation(project(":hashing"))
    implementation(project(":files"))
    implementation(project(":snapshots"))

    implementation(libs.asm)
    implementation(libs.guava)
    implementation(libs.slf4jApi)
    implementation(libs.commonsIo)

    testImplementation(project(":base-services"))
    testImplementation(project(":internal-testing"))

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
