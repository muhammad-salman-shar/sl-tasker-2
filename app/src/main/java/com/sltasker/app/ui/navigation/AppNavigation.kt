package com.sltasker.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// =============================================================================
// AppNavigation.kt — the app's NavHost + placeholder destinations.
//
// Each tab currently renders a minimal PlaceholderScreen; real screens will
// replace these composables as features land (Home is intentionally NOT built
// in this foundation step).
// =============================================================================

/** Registers all top-level tab destinations on the given [navController]. */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.START_ROUTE,
        modifier = modifier,
    ) {
        tabDestinations()
    }
}

/**
 * Extension keeps destination registration tidy and reusable if we later add
 * nested graphs (e.g. task detail pushed from the Tasks tab).
 */
fun NavGraphBuilder.tabDestinations() {
    composable(BottomNavItem.Home.route) {
        PlaceholderScreen("Home Screen")
    }
    composable(BottomNavItem.Tasks.route) {
        PlaceholderScreen("Tasks Screen")
    }
    composable(BottomNavItem.Alarm.route) {
        PlaceholderScreen("Alarm Screen")
    }
    composable(BottomNavItem.Player.route) {
        PlaceholderScreen("Player Screen")
    }
}

/** Temporary full-screen placeholder until feature screens are implemented. */
@Composable
private fun PlaceholderScreen(title: String) {
    // Background comes from the Scaffold in MainActivity (BackgroundBase);
    // the Box just centres the label.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White // == theme TextPrimary, on the OLED background
        )
    }
}
