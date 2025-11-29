package com.vitiligo.breathe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vitiligo.breathe.domain.model.navigation.Screen
import com.vitiligo.breathe.ui.health.HealthScreen
import com.vitiligo.breathe.ui.home.HomeScreen
import com.vitiligo.breathe.ui.location_details.LocationDetailsScreen
import com.vitiligo.breathe.ui.map.MapScreen
import com.vitiligo.breathe.ui.search.LocationSearchScreen

@Composable
fun BreatheNavHost(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(
                navigateToLocationDetails = { navController.navigate(Screen.LocationDetails(it)) },
                navigateToLocationSearch = { navController.navigate(Screen.LocationSearch) }
            )
        }

        composable<Screen.LocationDetails> {
            LocationDetailsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = {
                    navController.popBackStack(Screen.Home, inclusive = false)
                }
            )
        }

        composable<Screen.LocationSearch> {
            LocationSearchScreen(
                navigateBack = { navController.popBackStack() },
                navigateToLocationDetailsPreview = { latitude, longitude, placeId ->
                    navController.navigate(
                        Screen.LocationDetails(coordinates = "$latitude,$longitude", placeId = placeId)
                    )
                }
            )
        }

        composable<Screen.Map> {
            MapScreen()
        }

        composable<Screen.Health> {
            HealthScreen()
        }
    }
}