// =============================================================================
// build.gradle.kts (project root) — SL Tasker
// Declares shared plugins without applying them to every module.
// =============================================================================
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
