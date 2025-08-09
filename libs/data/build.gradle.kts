plugins {
    id("com.google.dagger.hilt.android")
    id("io.lb.android.library")
    kotlin("kapt")
}

android {
    namespace = "io.lb.schedule.data"
}

dependencies {
    implementation(project(":libs:common"))
    implementation(project(":libs:domain"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.hilt.compiler)
    kapt(libs.room.compiler)
}
