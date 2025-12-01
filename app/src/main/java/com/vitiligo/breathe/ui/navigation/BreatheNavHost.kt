package com.vitiligo.breathe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.vitiligo.breathe.ui.report.ReportScreen
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
        enterTransition = {
            if (isBottomNavSwitch(initialState.destination.route, targetState.destination.route)) {
                fadeIn(animationSpec = tween(500))
            } else {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            }
        },
        exitTransition = {
            if (isBottomNavSwitch(initialState.destination.route, targetState.destination.route)) {
                fadeOut(animationSpec = tween(500))
            } else {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500))
            }
        },
        popEnterTransition = {
            if (isBottomNavSwitch(initialState.destination.route, targetState.destination.route)) {
                fadeIn(animationSpec = tween(500))
            } else {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            }
        },
        popExitTransition = {
            if (isBottomNavSwitch(initialState.destination.route, targetState.destination.route)) {
                fadeOut(animationSpec = tween(500))
            } else {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500))
            }
        },
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
            MapScreen(
                navigateToLocationDetailsPreview = { latitude, longitude ->
                    navController.navigate(
                        Screen.LocationDetails(coordinates = "$latitude,$longitude", placeId = "$latitude,$longitude")
                    )
                },
                navigateToCommunityReport = { latitude, longitude ->
                    navController.navigate(
                        Screen.CommunityReport(latitude, longitude)
                    )
                }
            )
        }

        composable<Screen.Health> {
            HealthScreen()
        }

        composable<Screen.CommunityReport> {
            ReportScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}

private fun isBottomNavSwitch(initialRoute: String?, targetRoute: String?): Boolean {
    if (initialRoute == null || targetRoute == null) return false

    val bottomNavRoutes = listOf(
        Screen.Home::class.qualifiedName,
        Screen.Map::class.qualifiedName,
        Screen.Health::class.qualifiedName
    )

    val isInitialBottom = bottomNavRoutes.any { initialRoute.contains(it ?: "Unknown") }
    val isTargetBottom = bottomNavRoutes.any { targetRoute.contains(it ?: "Unknown") }

    return isInitialBottom && isTargetBottom
}