plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.universal_yoga_admin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.universal_yoga_admin"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dataBinding { enable = true }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("androidx.sqlite:sqlite-ktx:2.4.0")
    implementation("androidx.sqlite:sqlite-framework:2.4.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")


    //lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    //lottie
    implementation("com.airbnb.android:lottie:4.2.2")

    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")

    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.49")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.49")

    // For local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:2.49")
    kaptTest("com.google.dagger:hilt-compiler:2.49")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}