package com.vitiligo.breathe.presentation.health

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column {
                Text("Health & Monitoring", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Protect sensitive groups with smart alerts",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        modifier = modifier
    )
}
