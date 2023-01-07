@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.createNativeBuildSystemVariantConfig

plugins {
    id("com.android.library")
}

android {
    namespace = "de.blinkt.openvpn"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        @Suppress("DEPRECATION")
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        aidl = true
        buildConfig = true
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("com.google.android.gms:play-services-ads-lite:21.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
}
