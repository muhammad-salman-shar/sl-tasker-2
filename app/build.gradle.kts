// =============================================================================
// app/build.gradle.kts — SL Tasker
// Solo Leveling inspired gamified productivity app.
// Foundation module: Compose + Material 3 + Navigation + Room + Retrofit/Moshi
// =============================================================================

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)      // Compose Compiler Gradle plugin (Kotlin 2.x)
    alias(libs.plugins.ksp)                 // Symbol processing for Room compiler
}

android {
    namespace = "com.sltasker.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sltasker.app"
        minSdk = 26            // OLED dark / edge-to-edge friendly baseline
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    // Java / Kotlin bytecode target — Java 17 required by modern AGP + Compose
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    // Keep generated Room schema JSON under app/schemas while iterating
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    // ---------------------------------------------------------------------
    // Core / Lifecycle
    // ---------------------------------------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)   // collectAsStateWithLifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose) // viewModel() in Compose
    implementation(libs.androidx.activity.compose)

    // ---------------------------------------------------------------------
    // Jetpack Compose (versions governed by the BOM)
    // ---------------------------------------------------------------------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)     // Full icon set for nav items

    // ---------------------------------------------------------------------
    // Navigation
    // ---------------------------------------------------------------------
    implementation(libs.androidx.navigation.compose)

    // ---------------------------------------------------------------------
    // Room (local persistence) — compiler wired through KSP
    // ---------------------------------------------------------------------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ---------------------------------------------------------------------
    // Coroutines
    // ---------------------------------------------------------------------
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // ---------------------------------------------------------------------
    // Networking: Retrofit + Moshi (for the AI API layer later)
    // ---------------------------------------------------------------------
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    implementation(libs.okhttp.logging.interceptor)

    // ---------------------------------------------------------------------
    // DataStore Preferences (user settings, onboarding flags, etc.)
    // ---------------------------------------------------------------------
    implementation(libs.androidx.datastore.preferences)

    // ---------------------------------------------------------------------
    // Testing
    // ---------------------------------------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)             // Compose preview support
    debugImplementation(libs.androidx.ui.test.manifest)       // Idling resources for tests
}
