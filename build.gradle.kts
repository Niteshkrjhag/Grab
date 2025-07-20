// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

//    id("org.jetbrains.kotlin.android") version "2.0.20" apply false

    alias(libs.plugins.androidApplication) apply false
    id ("com.android.library") version "8.5.1" apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
}
allprojects {
    repositories {
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}