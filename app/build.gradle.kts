plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "com.almanza.kochappi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.almanza.kochappi"
        minSdk = 26     // Android 8.0
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            // Aquí puedes configurar un buildConfigField para la URL base de tu API
            // buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/\"")
        }
    }

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

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    // ── Core ──
    implementation(libs.core.ktx)

    // ── Compose (BOM maneja las versiones) ──
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // ── Lifecycle ──
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)       // collectAsStateWithLifecycle
    implementation(libs.lifecycle.viewmodel.compose)     // viewModel()

    // ── Navigation ──
    implementation(libs.navigation.compose)

    // ── Hilt (DI) ──
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)         // hiltViewModel()

    // ── Room (DB Local) ──
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // ── DataStore (Preferences: token, sesión, rol) ──
    implementation(libs.datastore.preferences)

    // ── Retrofit + OkHttp (HTTP Client) ──
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // ── Kotlinx Serialization ──
    implementation(libs.kotlinx.serialization.json)

    // ── Coil (Image Loading para Compose) ──
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // ── Coroutines ──
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)

    // ── Testing ──
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
}
