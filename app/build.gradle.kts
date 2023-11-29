plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.20"
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "vn.com.dsk.demo.base.health_index"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.com.dsk.demo.base.health_index"
        minSdk = 26
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
        //noinspection DataBindingWithoutKapt
        dataBinding = true
        buildConfig = true
    }
    sourceSets {

    }
    kapt {
        generateStubs = true
    }
}

dependencies {
//    core
    implementation("androidx.core:core-ktx:1.12.0-rc01")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.8.0-alpha07")
//    material
    implementation("com.google.android.material:material:1.11.0-alpha02")
    implementation("androidx.compose.material3:material3:1.2.0-alpha06")
//    layout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    nav
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")
//    recycle view
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
//    data binding
    implementation("androidx.databinding:databinding-runtime:8.1.1")
    implementation("com.google.android.gms:play-services-analytics:18.0.3")
    implementation("androidx.core:core-animation:1.0.0-rc01")
//    test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
//    Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
//
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.47")
//
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//
//    implementation("com.jakewharton.timber:timber:5.0.1")
//  Room components
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("androidx.room:room-common:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
//
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
//
    implementation("com.google.firebase:firebase-storage:20.2.1")
//
    implementation("de.hdodenhof:circleimageview:3.1.0")
//
    implementation("androidx.core:core-splashscreen:1.0.1")
//
    implementation("com.beust:klaxon:5.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    implementation("com.google.code.gson:gson:2.10.1")
}
dependencies {
    val pagingVersion = "3.2.0"

    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

    // alternatively - without Android dependencies for tests
    testImplementation("androidx.paging:paging-common-ktx:$pagingVersion")

    // optional - RxJava2 support
    implementation("androidx.paging:paging-rxjava2-ktx:$pagingVersion")

    // optional - RxJava3 support
    implementation("androidx.paging:paging-rxjava3:$pagingVersion")

    // optional - Guava ListenableFuture support
    implementation("androidx.paging:paging-guava:$pagingVersion")

    // optional - Jetpack Compose integration
    implementation("androidx.paging:paging-compose:3.2.0")
//
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}