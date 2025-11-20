package com.vitiligo.breathe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vitiligo.breathe.ui.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun BreatheNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                navController
            )
        }
    }
}