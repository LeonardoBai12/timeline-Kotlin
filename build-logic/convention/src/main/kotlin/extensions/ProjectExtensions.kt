package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.accessors.dm.LibrariesForLibs
import provider.libs
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.accessors.runtime.extensionOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import provider.versionOf

/**
 * Configures the Kotlin settings for the project.
 *
 * @receiver The project to configure the Kotlin settings for.
 * @param commonExtension The common extension to configure the Kotlin settings for.
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = versionOf("compileSdk")

        defaultConfig {
            minSdk = versionOf("minSdk")
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = COMPILE_VERSION
            targetCompatibility = COMPILE_VERSION
        }

        buildTypes {
            getByName("debug") {
                enableUnitTestCoverage = true
            }
            getByName("release") {
                enableUnitTestCoverage = true
            }
        }
    }

    configureKotlin()
}

/**
 * Configures the Kotlin settings for the project.
 *
 * @receiver The project to configure the Kotlin settings for.
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = COMPILE_VERSION
        targetCompatibility = COMPILE_VERSION
    }

    configureKotlin()
}

/**
 * Configures the Kotlin settings for the project.
 *
 * @receiver The project to configure the Kotlin settings for.
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = COMPILE_VERSION.toString()
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}