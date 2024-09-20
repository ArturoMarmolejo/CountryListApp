plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.arturomarmolejo.countrylistapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arturomarmolejo.countrylistapp"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
    // optional - Multi process support
    implementation(libs.androidx.work.multiprocess)

    //Retrofit
    implementation(libs.retrofit)
    //Gson Converter
    implementation(libs.converter.gson)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    //OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //Mockk
    testImplementation(libs.mockk)

    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)

    //recycler view
    implementation(libs.androidx.recyclerview)

}