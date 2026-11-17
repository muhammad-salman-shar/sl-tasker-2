package com.sltasker.app.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sltasker.app.ui.components.GlassBackdrop
import com.sltasker.app.ui.theme.GlassBorderEnd
import com.sltasker.app.ui.theme.GlassBorderStart
import com.sltasker.app.ui.theme.GlassFill
import com.sltasker.app.ui.theme.GlowCyan
import com.sltasker.app.ui.theme.TextSecondary

// =============================================================================
// FloatingBottomNav.kt — the pill-shaped, frosted, floating bottom bar.
//
// Design notes:
//  • Floats above the system navigation bar (adds the navigationBars inset as
//    bottom padding so it never sits under the gesture pill).
//  • Glass = blurred backdrop plate + translucent fill + gradient border.
//  • A single cyan "pill" indicator SLIDES between tabs. Its horizontal offset
//    is driven by animateDpAsState with a spring spec; because every tab slot
//    has the same fixed width, the exact x-offset is pure math from the
//    selected index — no on-screen measurement required.
// =============================================================================

private val BarShape = RoundedCornerShape(28.dp)
private val TabSlotWidth = 76.dp   // every tab occupies an equal-width slot
private val IndicatorWidth = 60.dp
private val BarInnerPadding = 10.dp

/**
 * Floating glass bottom navigation bar.
 *
 * @param selectedItem  Route of the currently selected tab (derived from the
 *                      NavHost's current destination in MainActivity).
 * @param onTabSelected Invoked when the user taps a tab.
 */
@Composable
fun FloatingBottomNav(
    selectedItem: String,
    onTabSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = BottomNavItem.items
    val selectedIndex = items.indexOfFirst { it.route == selectedItem }
        .coerceAtLeast(0)

    // Keep the bar clear of the system gesture/nav bar while floating.
    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 12.dp + bottomInset),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(modifier = Modifier.height(64.dp)) {
            // Layer 1 — frosted backdrop plate (the Modifier.blur lives here).
            GlassBackdrop(
                modifier = Modifier.matchParentSize(),
                shape = BarShape,
                blurRadius = 24.dp
            )

            // Layer 2 — translucent fill + diagonal gradient border stroke.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(BarShape)
                    .background(GlassFill)
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(GlassBorderStart, GlassBorderEnd),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        shape = BarShape
                    )
            )

            // Layer 3 — sliding active-tab indicator (drawn UNDER the tabs).
            val slideOffset by animateDpAsState(
                targetValue = BarInnerPadding +
                    ((TabSlotWidth - IndicatorWidth) / 2) +
                    (TabSlotWidth * selectedIndex),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "navIndicatorSlide"
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = slideOffset)
                    .size(width = IndicatorWidth, height = 48.dp)
                    .clip(CircleShape)
                    .background(GlowCyan.copy(alpha = 0.16f))
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GlowCyan.copy(alpha = 0.55f),
                                GlowCyan.copy(alpha = 0.10f)
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        ),
                        shape = CircleShape
                    )
            )

            // Layer 4 — the tabs themselves, evenly spaced over the indicator.
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = BarInnerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    NavTab(
                        item = item,
                        selected = item.route == selectedItem,
                        onClick = { onTabSelected(item) }
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Single tab
// -----------------------------------------------------------------------------

@Composable
private fun NavTab(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // ripple would break the glass illusion
                role = Role.Tab,
                onClick = onClick
            )
            .semantics { contentDescription = item.label }
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = if (selected) GlowCyan else TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) GlowCyan else TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
