plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "br.com.ams.appcatalogo"
    compileSdk = 34

    defaultConfig {
        applicationId ="br.com.ams.appcatalogo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["auth0Domain"] = "@string/com_auth0_domain"
        manifestPlaceholders["auth0Scheme"] =  "demo"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding =  true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    ndkVersion ="25.2.9519653"
    buildToolsVersion ="34.0.0"
}


dependencies {

    val room_version by extra("2.6.1")
    val okhttp3_version by extra("4.10.0")
    val dagger_version by extra("2.5")
    val lifecycle_version by extra("2.6.2")
    val coroutines by extra ("1.3.9")

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.blankj:utilcodex:1.31.1")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

    // Retrofit & Gson
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation( "com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation( "org.greenrobot:eventbus:3.3.1")

    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Room components
    implementation ("androidx.room:room-ktx:$room_version")
    kapt ("androidx.room:room-compiler:$room_version")
    androidTestImplementation ("androidx.room:room-testing:$room_version")

    implementation ("com.google.dagger:dagger:2.50")
    kapt ("com.google.dagger:dagger-compiler:2.50")

    implementation ("com.auth0.android:auth0:2.+")


    implementation ("com.github.imagekit-developer.imagekit-android:imagekit-android:3.0.1")
    implementation ("com.github.imagekit-developer.imagekit-android:imagekit-picasso-extension:3.0.1")

    implementation ("com.squareup.okhttp3:okhttp:okhttp3_version")
    implementation ("com.squareup.okhttp3:logging-interceptor:${okhttp3_version}")


}