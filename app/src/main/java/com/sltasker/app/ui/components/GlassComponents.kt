package com.sltasker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sltasker.app.ui.theme.GlassBorderEnd
import com.sltasker.app.ui.theme.GlassBorderStart
import com.sltasker.app.ui.theme.GlassFill
import com.sltasker.app.ui.theme.GlassFillElevated
import com.sltasker.app.ui.theme.GlowCyan
import com.sltasker.app.ui.theme.TextPrimary
import com.sltasker.app.ui.theme.TextSecondary

// =============================================================================
// GlassComponents.kt — SL Tasker reusable "liquid glass" primitives
//
// The frosted look is built from three layers:
//   1. A blurred backdrop plate (Modifier.blur on a childless Box behind the
//      content) — blurring the container itself would blur its children too,
//      so the frost always lives on its own layer.
//   2. A translucent fill (GlassFill) + diagonal gradient border stroke
//      (GlassBorderStart -> GlassBorderEnd) faking the specular edge of glass.
//   3. Crisp, unblurred content on top.
//
// Public API:
//   • Modifier.glassSurface(...)  — fill + gradient border for any modifier chain
//   • GlassBackdrop(...)          — the blurred frost plate (use inside a Box)
//   • GlassCard { ... }           — standard content card (16.dp radius default)
//   • GlassPill(onClick = ...)    — round/pill button used by the bottom nav & CTAs
// =============================================================================

/** Diagonal highlight brush shared by every glass border in the app. */
private val GlassBorderBrush = Brush.linearGradient(
    colors = listOf(GlassBorderStart, GlassBorderEnd),
    start = Offset.Zero,
    end = Offset.Infinite
)

// -----------------------------------------------------------------------------
// 1. Core modifier
// -----------------------------------------------------------------------------

/**
 * Applies the liquid-glass treatment (translucent fill + gradient border) to
 * any modifier chain. Pair with [GlassBackdrop] placed behind the node when
 * you want the full frosted effect.
 *
 * @param shape       Corner shape (default: 16.dp rounded, per design system).
 * @param borderWidth Thickness of the gradient border stroke.
 * @param borderColor Optional solid/tinted border override (e.g. GlowCyan).
 */
fun Modifier.glassSurface(
    shape: Shape = RoundedCornerShape(16.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color? = null,
): Modifier = this
    .clip(shape)
    .background(GlassFill, shape)
    .border(
        width = borderWidth,
        brush = borderColor?.let { Brush.linearGradient(listOf(it, it.copy(alpha = 0.25f))) }
            ?: GlassBorderBrush,
        shape = shape
    )

// -----------------------------------------------------------------------------
// 2. Blurred backdrop plate
// -----------------------------------------------------------------------------

/**
 * The frosted layer of a glass surface. Place as the FIRST child of a `Box`
 * and lay real content above it — never blur a container that has children,
 * or the children render blurred as well.
 *
 * @param modifier   Size it to the parent (e.g. `Modifier.matchParentSize()`).
 * @param shape      Must match the shape of the surface drawn on top of it.
 * @param blurRadius Strength of the frost (20.dp reads well on OLED black).
 */
@Composable
fun GlassBackdrop(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    blurRadius: Dp = 20.dp,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(GlassFillElevated)
            .blur(blurRadius) // <- the actual Modifier.blur frosted effect
    )
}

// -----------------------------------------------------------------------------
// 3. GlassCard — standard content card
// -----------------------------------------------------------------------------

/**
 * Reusable frosted content card.
 *
 * ```
 * GlassCard(modifier = Modifier.fillMaxWidth()) {
 *     Text("100 Push-ups", style = MaterialTheme.typography.titleMedium)
 *     Text("+50 XP", style = MaterialTheme.typography.labelMedium)
 * }
 * ```
 *
 * @param onClick        Optional click handler; makes the card tappable with a
 *                       subtle press-dim instead of a ripple (keeps glass clean).
 * @param shape          Corner shape — 16.dp radius by default.
 * @param blurRadius     Frost strength of the backdrop layer.
 * @param contentPadding Inner padding (16.dp default).
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(16.dp),
    blurRadius: Dp = 20.dp,
    contentPadding: Dp = 16.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.72f else 1f,
        label = "glassCardPressAlpha"
    )

    val clickable = if (onClick != null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null, // no ripple on glass
            role = Role.Button,
            onClick = onClick
        )
    } else Modifier

    Box(modifier = modifier.graphicsLayer { alpha = pressAlpha }) {
        // Layer 1: blurred frost plate behind everything.
        GlassBackdrop(
            modifier = Modifier.matchParentSize(),
            shape = shape,
            blurRadius = blurRadius
        )
        // Layers 2+3: fill/border + crisp content.
        Column(
            modifier = Modifier
                .then(clickable)
                .glassSurface(shape = shape)
                .padding(contentPadding),
            verticalArrangement = verticalArrangement
        ) {
            content(this)
        }
    }
}

// -----------------------------------------------------------------------------
// 4. GlassPill — floating pill button (bottom-nav tabs, chips, CTAs)
// -----------------------------------------------------------------------------

/**
 * Compact frosted pill with icon (+ optional label). Used as the sliding
 * active-tab indicator inside the floating bottom navigation bar, and usable
 * standalone for filter chips / small actions.
 *
 * @param selected  Drives the glow tint + text color of the pill.
 * @param glowColor Accent used when selected (defaults to signature cyan).
 */
@Composable
fun GlassPill(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    label: String? = null,
    glowColor: Color = GlowCyan,
    shape: Shape = CircleShape,
    onClick: () -> Unit = {},
) {
    val selection by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        label = "glassPillSelection"
    )

    Row(
        modifier = modifier
            .clip(shape)
            .background(glowColor.copy(alpha = 0.18f * selection))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = if (label != null) 18.dp else 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) glowColor else TextSecondary
        )
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = if (selected) TextPrimary else TextSecondary
            )
        }
    }
}
