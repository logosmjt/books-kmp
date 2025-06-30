@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sql.delight)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
        }
    }

    wasmJs {
        browser {
            binaries.executable()
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            transitiveExport = true
            compilations.all {
                kotlinOptions.freeCompilerArgs += arrayOf("-linker-options", "-lsqlite3")
            }
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.ktor.common)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.material3)
            implementation(libs.material3.window.size.multiplatform)
            implementation(libs.compose.material)
            implementation(libs.koin.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.material.icons.core)
            implementation(libs.multiplatform.settings)

            implementation(libs.bundles.kamel.common)
            implementation(libs.navigation.compose)
        }

        commonTest.dependencies {
            implementation(libs.bundles.shared.commonTest)
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okHttp)
                implementation(libs.sql.android.driver)
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
                implementation(libs.bundles.kamel.android)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(libs.bundles.shared.androidTest)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.ios)
                implementation(libs.sql.native.driver)
                implementation(libs.components.resources)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(compose.foundation)
                implementation(compose.components.resources)
                implementation(libs.sql.web.worker.driver)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.simple.books"
    compileSdk = 35
    defaultConfig {
        minSdk = 27
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    implementation(libs.androidx.core)
}

sqldelight {
    databases {
        create(name = "BooksDatabase") {
            packageName.set("com.simple.books.db")
        }
    }
}
