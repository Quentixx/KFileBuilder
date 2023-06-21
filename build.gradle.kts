import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
}

group = "fr.quentixx"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()

    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()

}

dependencies {

    implementation("ch.qos.logback:logback-classic:1.4.8")
    implementation("io.github.microutils:kotlin-logging:3.0.5")



    implementation(compose.desktop.currentOs)

    testImplementation(kotlin("test"))
}

//tasks.test {
//    useJUnitPlatform()
//}
//
//kotlin {
//    jvmToolchain(11)
//}
//
//application {
//    mainClass.set("MainKt")
//}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinJvmComposeDesktopApplication"
            packageVersion = "1.0.0"
        }
    }
}