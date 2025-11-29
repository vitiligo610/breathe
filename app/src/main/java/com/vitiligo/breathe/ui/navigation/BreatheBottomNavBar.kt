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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
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
    val currentDestination = remember(navBackStackEntry) {
        Screen.fromRoute(navBackStackEntry?.destination?.route ?: "")
    }

    var selectedDestinationIndex by rememberSaveable { mutableIntStateOf(0) }

    val isBottomBarVisible = bottomNavItems.any { item ->
        currentDestination == item.screen
    }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                BreatheBottomNavBar(
                    navController = navController,
                    selectedDestinationIndex = selectedDestinationIndex,
                    setSelectedDestinationIndex = { selectedDestinationIndex = it }
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
    selectedDestinationIndex: Int,
    setSelectedDestinationIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier
    ) {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedDestinationIndex == index,
                onClick = {
                    navController.navigate(item.screen)
                    setSelectedDestinationIndex(index)
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
