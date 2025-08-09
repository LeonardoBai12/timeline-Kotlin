import extensions.configureKotlinJvm
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import provider.libs

/**
 * Plugin to apply JVM library conventions.
 */
class JvmLibraryConventionPlugin : Plugin<Project> {
    /**
     * Applies the JVM library conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<KotlinJvmProjectExtension> {
                configureKotlinJvm()
            }

            dependencies {
                with(libs) {
                    // Coroutines
                    implementation(kotlinx.coroutines.core)
                    implementation(kotlinx.coroutines.android)

                    // Testing
                    testImplementation(mockk)
                    testImplementation(junit.jupiter.api)
                    testImplementation(junit.jupiter.params)
                    testImplementation(kotlinx.coroutines.test)
                }
            }
        }
    }
}
