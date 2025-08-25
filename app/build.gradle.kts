plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "bm.babimumba.diabete"
    compileSdk = 35

    defaultConfig {
        applicationId = "bm.babimumba.diabete"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")
    implementation("de.hdodenhof:circleimageview:3.1.0")               //Circle image
    implementation ("com.github.bumptech.glide:glide:4.16.0")          //Glide image
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
  /*  implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:custom-ui:12.1.0")*/
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //Gson
    implementation("com.google.code.gson:gson:2.8.8")

    //SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // view model
    implementation("androidx.fragment:fragment-ktx:1.8.8")

    //zxing for QR code generation
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    //iText 7
    implementation ("com.itextpdf:itext7-core:7.2.5") // Vérifie la dernière version sur le site d'iText
    implementation ("com.itextpdf:kernel:7.2.5")
    implementation ("com.itextpdf:layout:7.2.5")
    implementation ("com.itextpdf:io:7.2.5")
    //lecteur de pdf
    //implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation ("com.github.DImuthuUpe:AndroidPdfViewer:2.8.1")
    
    //OkHttp pour les appels réseau
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}