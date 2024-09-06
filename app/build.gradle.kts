plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compilerKsp)
    id("com.google.dagger.hilt.android") version "2.48"
    alias(libs.plugins.kotlinPluginParcelize)

}

android {
    namespace = "org.sniffsnirr.skillcinema"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.sniffsnirr.skillcinema"
        minSdk = 24
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.datastore)

    implementation(libs.okhttp3)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.retrofit)
    implementation(libs.retrofit)
    implementation(libs.gson)

    implementation (libs.hiltAndroid)
    implementation(libs.hiltNavigation)
    implementation(libs.treeTeenABP)
    ksp (libs.hiltCompiler)

    implementation(libs.glide)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)

    implementation(libs.androidx.paging)

    implementation (libs.expandabletextview)

    implementation(libs.android.flexbox)
implementation(libs.glide.slider)
}