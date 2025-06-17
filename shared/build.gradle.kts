import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.android)
            implementation(libs.sqlDelight.android.driver)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.junit)
            implementation(kotlin("test-common")) // Base Kotlin test annotations
            implementation(kotlin("test-annotations-common"))
        }
    }
}

val properties = Properties()
val propertiesFile = project.rootProject.file("local.properties")
if (propertiesFile.exists()) {
    properties.load(FileInputStream(propertiesFile))
}

val apiKey: String = properties.getProperty("apiKey") ?: ""

android {
    namespace = "com.example.w3wthemovie"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }
        debug {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }
    }
}

sqldelight {
    databases {
        create("W3WTheMovieDatabase") {
            packageName.set("com.example.w3wthemovie")
        }
    }
}
