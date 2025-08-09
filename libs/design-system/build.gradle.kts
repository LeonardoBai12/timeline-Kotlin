import extensions.implementation

plugins {
    id("io.lb.android.library")
    kotlin("kapt")
}

android {
    namespace = "io.lb.schedule.designsystem"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":libs:common"))
}