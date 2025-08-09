plugins {
    id("com.google.dagger.hilt.android")
    id("io.lb.android.app")
    kotlin("kapt")
}

android {
    defaultConfig {
        applicationId = "io.lb.schedule"
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    kapt {
        correctErrorTypes = true
    }
    namespace = "io.lb.schedule"
}

dependencies {
    implementation(project(":libs:common"))
    implementation(project(":libs:design-system"))
    implementation(project(":libs:data"))
    implementation(project(":libs:domain"))
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}