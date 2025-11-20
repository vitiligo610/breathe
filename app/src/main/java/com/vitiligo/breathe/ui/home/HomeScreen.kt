package com.vitiligo.breathe.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.vitiligo.breathe.presentation.home.HomeContent
import com.vitiligo.breathe.presentation.home.HomeTopBar

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { HomeTopBar() },
        modifier = modifier
    ) {
        HomeContent(
            modifier = Modifier
                .padding(it)
        )
    }
}