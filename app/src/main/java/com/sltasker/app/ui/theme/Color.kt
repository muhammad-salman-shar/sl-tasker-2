package com.sltasker.app.ui.theme

import androidx.compose.ui.graphics.Color

// =============================================================================
// Color.kt — SL Tasker "OLED Dark Liquid Glass" palette
// A deep, near-black base with translucent glass surfaces and neon glow accents,
// inspired by the Solo Leveling system-window aesthetic.
// =============================================================================

// --- Base / Surfaces ----------------------------------------------------------
/** Near-pure black-blue OLED background. Keeps glows punchy and battery-friendly. */
val BackgroundBase = Color(0xFF05070E)

/** Translucent fill used for every glass surface (cards, pills, sheets). */
val GlassFill = Color(0xFF121B2E).copy(alpha = 0.55f)

/** Slightly denser glass for elevated layers (dialogs, active nav pill). */
val GlassFillElevated = Color(0xFF182338).copy(alpha = 0.75f)

// --- Glass borders ------------------------------------------------------------
/** Top-left highlight of the gradient border stroke on glass surfaces. */
val GlassBorderStart = Color.White.copy(alpha = 0.25f)

/** Bottom-right fade of the gradient border stroke on glass surfaces. */
val GlassBorderEnd = Color.White.copy(alpha = 0.05f)

// --- Neon glows ---------------------------------------------------------------
/** Signature "system window" cyan — primary accent, focus rings, active states. */
val GlowCyan = Color(0xFF00D2FF)

/** Arcane violet — secondary accent, gradients, level-up effects. */
val GlowViolet = Color(0xFF7B2CBF)

// --- Text ---------------------------------------------------------------------
/** Primary text on dark glass. */
val TextPrimary = Color.White

/** Muted blue-grey for captions, hints, inactive labels. */
val TextSecondary = Color(0xFF94A3B8)

// --- Semantic / gamification accents -------------------------------------------
/** XP gold — rewards, streaks, achievements. */
val AmberGold = Color(0xFFFBBF24)

/** Quest blue — normal tasks / informational state. */
val RoyalBlue = Color(0xFF3B82F6)

/** Danger red — penalties, boss/urgent tasks, missed alarms. */
val CrimsonRed = Color(0xFFFF2A5F)

/** Success green — completed tasks, positive streaks. */
val EmeraldGreen = Color(0xFF34D399)
