package com.vitiligo.breathe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vitiligo.breathe.ui.home.HomeScreen
import com.vitiligo.breathe.ui.location_details.LocationDetailsScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class LocationDetails(
    val id: Int
)

@Composable
fun BreatheNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
    ) {
        composable<Home> {
            HomeScreen(
                navigateToLocationDetails = { navController.navigate(LocationDetails(it)) }
            )
        }

        composable<LocationDetails> {
            LocationDetailsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}