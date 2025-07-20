plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.example.first_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.first_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
// Alternatively - use the following artifact without an Android dependency.
dependencies {

    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.firebase.ai)

    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    // Core Android dependencies
    implementation ("androidx.core:core-ktx:1.13.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation ("androidx.activity:activity-compose:1.9.1")

    // Jetpack Compose dependencies
    implementation ("androidx.compose:compose-bom:2024.06.00")
    implementation ("androidx.compose.ui:ui")
    implementation ("androidx.compose.ui:ui-graphics")
    implementation ("androidx.compose.ui:ui-tooling-preview")
    implementation ("androidx.compose.material3:material3:1.3.0")

    // Jetpack Navigation Compose
    implementation ("androidx.navigation:navigation-compose:2.7.7")

    // Hilt dependencies
    implementation ("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.compose.android)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    kapt ("com.google.dagger:hilt-compiler:2.51.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Testing dependencies
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1") //3.6.1
    androidTestImplementation ("androidx.compose:compose-bom:2024.06.00")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.7.0-beta06")
    debugImplementation ("androidx.compose.ui:ui-tooling")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")

    // Preferences DataStore
    implementation ("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-core:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-rxjava2:1.1.1")
    implementation ("androidx.datastore:datastore-preferences-rxjava3:1.1.1")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Kotlinx Serialization
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Coil
    implementation ("io.coil-kt:coil-compose:2.7.0")

    // Compose Foundation
    implementation ("androidx.compose.foundation:foundation:1.6.8")

    val lifecycle_version = "2.8.4"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    //Splash Api
    implementation (libs.androidx.core.splashscreen)

    //Accompanist
    implementation (libs.accompanist.systemuicontroller)

    //Paging 3
    implementation ("androidx.paging:paging-runtime:3.3.2")
    implementation ("androidx.paging:paging-compose:3.3.2")

    //Firebase required dependency
    implementation(libs.firebase.auth)
        implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
//        implementation("com.google.firebase:firebase-auth")

    //Google sign in required dependency
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation (libs.play.services.auth)
    implementation (libs.googleid)


    //Gemini sdk for android
//    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.google.firebase:firebase-ai")
    implementation("com.google.firebase:firebase-analytics")

    //LottieFiles
    implementation ("com.airbnb.android:lottie-compose:6.5.2")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.28.0")

    implementation("androidx.compose.animation:animation")

    // You might also need animation-core for more advanced animations
    implementation("androidx.compose.animation:animation-core")


}
kapt {
    correctErrorTypes = true
}