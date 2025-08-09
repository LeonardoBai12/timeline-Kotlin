plugins {
    id("io.lb.jvm.library")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":libs:common"))
}
