package com.sltasker.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// =============================================================================
// Theme.kt — SL Tasker theme
// The app is dark-only (OLED liquid glass aesthetic), so we ship a single
// hand-tuned dark ColorScheme. `isSystemInDarkTheme()` is kept in the
// signature only to silence lint about unused theming modes elsewhere.
// =============================================================================

/**
 * Material 3 color scheme mapped from the SL palette.
 *
 * Note: most glass surfaces in SL Tasker are drawn manually (see
 * ui/components/GlassComponents.kt) rather than via M3 container colors —
 * this scheme mainly drives text, icons and default component tinting.
 */
private val SLDarkColorScheme = darkColorScheme(
    // Surfaces
    primary = GlowCyan,
    onPrimary = BackgroundBase,
    primaryContainer = GlowViolet,
    onPrimaryContainer = TextPrimary,

    secondary = GlowViolet,
    onSecondary = TextPrimary,
    secondaryContainer = GlassFillElevated,
    onSecondaryContainer = TextSecondary,

    tertiary = AmberGold,
    onTertiary = BackgroundBase,

    background = BackgroundBase,   // <- OLED base as required
    onBackground = TextPrimary,
    surface = BackgroundBase,
    onSurface = TextPrimary,
    surfaceVariant = GlassFillElevated,
    onSurfaceVariant = TextSecondary,

    error = CrimsonRed,
    onError = TextPrimary,
    outline = TextSecondary,
    outlineVariant = GlassBorderEnd
)

/**
 * App-wide theme wrapper.
 *
 * @param content The composable subtree rendered under the theme.
 */
@Composable
fun SLTaskerTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Always off: brand palette > Material You
    content: @Composable () -> Unit
) {
    val colorScheme = SLDarkColorScheme

    // Force light status-bar icons (white) while the app window is dark,
    // so system bars stay readable over BackgroundBase.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SLTypography,
        shapes = SLShapes,
        content = content
    )
}
