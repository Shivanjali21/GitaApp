plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.devtoolsKSP)
    alias(libs.plugins.navSafeArgs)
}

android {
    namespace = "com.practice.shreebhagavadgita"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.practice.shreebhagavadgita"
        minSdk = 23
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Lottie
    implementation(libs.lottieAnim)
    //Navigation Components
    implementation(libs.navFragmentKtx)
    implementation(libs.navUiKtx)
    // Coroutines
    implementation(libs.ktCoroutineAndroid)
    //Glide
    implementation(libs.glide)
    //ViewModel
    implementation(libs.lifeCycleVMKt)
    // LiveData
    implementation(libs.lifeCycleLiveData)
    implementation(libs.lifeCycleRuntimekt)
    //Timber
    implementation(libs.timer)
    //Retrofit
    implementation(libs.squareRetrofit)
    implementation(libs.converterGson)
    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compile)
    //views dimension
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    //shimmer
    implementation(libs.shimmer)
}