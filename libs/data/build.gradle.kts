plugins {
    id("io.lb.jvm.library")
}

dependencies {
    implementation(project(":libs:common"))
    implementation(libs.kotlinx.serialization.json)
}
