buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    /* COMMENTED OUT FIREBASE DEPENDENCY UNTIL IT'S FULLY SET UP (OTHERWISE COMPILE ERRORS)

    id("com.google.gms.google-services") version "4.3.15" apply false

}