package com.vitiligo.breathe.presentation.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.vitiligo.breathe.presentation.shared.AppLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = { AppLogo() }
    )
}