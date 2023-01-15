@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("android")
    id("com.android.application")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "gcu.product.supplevpn"
    compileSdk = 33

    defaultConfig {
        applicationId = "gcu.product.supplevpn"
        minSdk = 21
        targetSdk = 33
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Modules
    implementation(project(":gateway"))
    implementation(project(":usecase"))
    implementation(project(":base"))
    implementation(project(":engine"))

    // Core
    val composeBomVersion = "2022.12.00"
    val kotlinCoreVersion = "1.9.0"
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
    platform("androidx.compose:compose-bom:$composeBomVersion")
    implementation("androidx.core:core-ktx:$kotlinCoreVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.activity:activity-compose:1.6.1")

    // UI
    val composeVersion = "1.3.2"
    val materialVersion = "1.3.1"
    val material3Version = "1.0.1"
    val splashVersion = "1.0.0"
    val coilVersion = "2.2.2"
    implementation("io.coil-kt:coil-svg:$coilVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.core:core-splashscreen:$splashVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-graphics:$composeVersion")
    implementation("androidx.compose.material3:material3:$material3Version")
    implementation("androidx.compose.material:material:$materialVersion")
    implementation("com.github.SimformSolutionsPvtLtd:SSJetPackComposeProgressButton:1.0.7")

    // Navigation
    val navigationVersion = "2.6.0-alpha04"
    implementation("androidx.navigation:navigation-compose:$navigationVersion")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.2")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    //  DI
    val hiltVersion = "2.44"
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    // Rest
    val okHttpVersion = "5.0.0-alpha.2"
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion")

    // RxJava
    val rxJavaVersion = "3.1.5"
    val rxAndroidVersion = "3.0.2"
    implementation("io.reactivex.rxjava3:rxjava:$rxJavaVersion")
    implementation("io.reactivex.rxjava3:rxandroid:$rxAndroidVersion")

}