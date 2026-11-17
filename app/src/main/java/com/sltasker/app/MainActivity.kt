package com.sltasker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sltasker.app.ui.navigation.AppNavigation
import com.sltasker.app.ui.navigation.BottomNavItem
import com.sltasker.app.ui.navigation.FloatingBottomNav
import com.sltasker.app.ui.theme.BackgroundBase
import com.sltasker.app.ui.theme.SLTaskerTheme

// =============================================================================
// MainActivity.kt — single-activity Compose entry point for SL Tasker.
//
// Edge-to-edge strategy:
//  • enableEdgeToEdge() makes the window draw behind both system bars.
//  • The Scaffold consumes the STATUS-BAR inset as top padding, so screen
//    content never hides under the clock/battery.
//  • The bottom bar is NOT a Scaffold `bottomBar` (that would pin it and eat
//    the inset). Instead FloatingBottomNav floats in the Box below and adds
//    the navigation-bar inset itself — keeping the pill truly floating.
//  • NavHost gets bottom padding equal to the bar height so placeholder
//    content can't be occluded by the glass bar.
// =============================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Draw behind the status & navigation bars (translucent system bars).
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SLTaskerTheme {
                SLTaskerApp()
            }
        }
    }
}

@Composable
fun SLTaskerApp(
    navController: NavHostController = rememberNavController(),
) {
    // Observe the current destination so the floating bar knows the active tab.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: BottomNavItem.START_ROUTE

    Scaffold(
        // OLED base shows through the transparent system bars.
        containerColor = BackgroundBase,
        // Consume only the TOP inset here; the bottom is handled by the
        // floating bar (which adds the navigation-bar inset itself) plus the
        // reserved bar height below, so nothing double-counts.
        contentWindowInsets = WindowInsets.statusBars,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBase)
        ) {
            // --- Screen host ---------------------------------------------------
            AppNavigation(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    // Status bar clearance comes from the Scaffold insets…
                    .padding(top = innerPadding.calculateTopPadding())
                    // …and the floating bar's footprint is reserved at the bottom
                    // (bar height 64dp + 12dp float gap + system nav inset).
                    .padding(bottom = 76.dp + innerPadding.calculateBottomPadding())
            )

            // --- Floating glass bottom navigation ------------------------------
            FloatingBottomNav(
                selectedItem = currentRoute,
                onTabSelected = { item ->
                    // Single-top-instance behaviour for tab switching:
                    // pop back to start, restore state, no duplicate entries.
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
