package com.vitiligo.breathe.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vitiligo.breathe.presentation.home.HomeContent
import com.vitiligo.breathe.presentation.home.HomeTopBar

@Composable
fun HomeScreen(
    navigateToLocationDetails: (Int) -> Unit,
    navigateToLocationSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { HomeTopBar(navigateToLocationSearch) },
        modifier = modifier
    ) {
        HomeContent(
            navigateToLocationDetails = navigateToLocationDetails,
            modifier = Modifier
                .padding(it)
        )
    }
}