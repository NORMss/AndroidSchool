import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.eltex.androidschool"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.eltex.androidschool"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val secretProps = rootDir.resolve("secrets.properties")
            .bufferedReader()
            .use {
                Properties().apply {
                    load(it)
                }
            }
        buildConfigField("String", "API_KEY", secretProps.getProperty("API_KEY"))
        buildConfigField("String", "USER_TOKEN", secretProps.getProperty("USER_TOKEN"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        sourceSets.all {
            languageSettings.enableLanguageFeature("ExplicitBackingFields")
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.kotlinx.coroutines.bom))

    implementation(libs.kotlinx.coroutines.core)

    coreLibraryDesugaring(libs.desugarJdkLibs)

    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.rxkotlin)
    implementation(libs.adapter.rxjava3)

    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.retrofit)
    implementation(platform(libs.okhttp.bom))

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.swiperefreshlayout)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.coil)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}