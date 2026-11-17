package com.sltasker.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

// =============================================================================
// BottomNavItem.kt — single source of truth for the bottom navigation tabs.
// Routes double as NavHost start-destination / navigate() targets.
// =============================================================================

sealed class BottomNavItem(
    /** Navigation route string (also used as the unique key). */
    val route: String,
    /** Short label shown under/inside the nav pill. */
    val label: String,
    /** Outlined icon — filled variants come later with selection animation. */
    val icon: ImageVector,
) {
    data object Home : BottomNavItem(route = "home", label = "Home", icon = Icons.Outlined.Home)
    data object Tasks : BottomNavItem(route = "tasks", label = "Tasks", icon = Icons.Outlined.Assignment)
    data object Alarm : BottomNavItem(route = "alarm", label = "Alarm", icon = Icons.Outlined.Alarm)
    data object Player : BottomNavItem(route = "player", label = "Player", icon = Icons.Outlined.Person)

    companion object {
        /** Ordered list rendered left-to-right in the floating nav bar. */
        val items: List<BottomNavItem> = listOf(Home, Tasks, Alarm, Player)

        /** Route of the screen shown on cold start. */
        const val START_ROUTE: String = Home.route
    }
}
