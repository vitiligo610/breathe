package com.vitiligo.breathe.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vitiligo.breathe.domain.model.navigation.BottomNavItem
import com.vitiligo.breathe.domain.model.navigation.Screen

val bottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = Icons.Default.Home,
        screen = Screen.Home
    ),
    BottomNavItem(
        label = "Map",
        icon = Icons.Default.Public,
        screen = Screen.Map
    ),
    BottomNavItem(
        label = "Health",
        icon = Icons.Default.Favorite,
        screen = Screen.Health
    )
)

@Composable
fun BreatheScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomBarVisible = currentDestination != null && bottomNavItems.any { item ->
        currentDestination.hierarchy.any { it.hasRoute(item.screen::class) }
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BreatheBottomNavBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        },
        modifier = modifier
    ) {
        BreatheNavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier
                .padding(bottom = it.calculateBottomPadding())
        )
    }
}

@Composable
fun BreatheBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(item.screen::class)
                } == true,
                onClick = {
                    navController.navigate(item.screen) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
