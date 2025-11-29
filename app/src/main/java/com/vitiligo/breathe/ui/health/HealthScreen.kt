package com.vitiligo.breathe.ui.health

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vitiligo.breathe.presentation.health.HealthContent
import com.vitiligo.breathe.presentation.health.HealthTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            HealthTopBar()
        },
        modifier = modifier
    ) {
        HealthContent(
            modifier = Modifier
                .padding(it)
        )
    }
}
