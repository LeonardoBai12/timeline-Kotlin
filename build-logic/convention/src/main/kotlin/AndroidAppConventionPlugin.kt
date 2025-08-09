import com.android.build.api.dsl.ApplicationExtension
import extensions.androidTestImplementation
import extensions.configureKotlinAndroid
import extensions.debugImplementation
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import provider.libs
import provider.versionOf

/**
 * Plugin to apply Android app conventions.
 */
class AndroidAppConventionPlugin : Plugin<Project> {
    /**
     * Applies the Android app conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = versionOf("targetSdk")
            }

            dependencies {
                with(libs) {
                    implementation(androidx.activity.compose)
                    implementation(compose.ui.ui)
                    implementation(compose.foundation)
                    implementation(compose.material3)
                    implementation(kotlinx.coroutines.android)
                    testImplementation(mockk)
                    testImplementation(junit.jupiter.api)
                    testImplementation(junit.jupiter.params)
                    testImplementation(assertk)
                    testImplementation(kotlinx.coroutines.test)
                    testImplementation(kotlin.test.junit)
                    androidTestImplementation(androidx.espresso.core)
                }
            }
        }
    }
}