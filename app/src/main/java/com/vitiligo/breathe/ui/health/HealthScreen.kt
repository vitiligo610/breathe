package com.vitiligo.breathe.ui.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.presentation.health.ConfigureZoneSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(
    viewModel: HealthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedZoneForEdit by remember { mutableStateOf<HealthZoneUiModel?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
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
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HealthDashboardHeader(activeCount = state.zones.count { it.settings?.isNotificationEnabled == true })
            }

            item {
                Text(
                    "Monitored Locations",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(state.zones) { zone ->
                HealthZoneCard(
                    zone = zone,
                    onConfigureClick = { selectedZoneForEdit = zone },
                    onToggleNotification = { enabled ->
                        viewModel.toggleNotification(zone.location.id, enabled)
                    }
                )
            }

            if (state.zones.isEmpty()) {
                item {
                    EmptyStateCard()
                }
            }
        }

        if (selectedZoneForEdit != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedZoneForEdit = null },
                sheetState = sheetState
            ) {
                ConfigureZoneSheetContent(
                    zone = selectedZoneForEdit!!,
                    onSave = { label, profile, enabled, interval ->
                        viewModel.saveZoneSettings(
                            selectedZoneForEdit!!.location.id,
                            label, profile, enabled, interval
                        )
                        selectedZoneForEdit = null
                    },
                    onCancel = { selectedZoneForEdit = null }
                )
            }
        }
    }
}

@Composable
fun HealthDashboardHeader(activeCount: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "$activeCount Active Zones",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Background monitoring is running",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun HealthZoneCard(
    zone: HealthZoneUiModel,
    onConfigureClick: () -> Unit,
    onToggleNotification: (Boolean) -> Unit
) {
    val isConfigured = zone.settings != null
    val isEnabled = zone.settings?.isNotificationEnabled == true

    Card(
        onClick = onConfigureClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isConfigured) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface,
        ),
        border = if (!isConfigured) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (isConfigured) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = zone.settings?.label?.display ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = zone.location.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (isConfigured) {
                    Text(
                        text = "Monitoring for: ${zone.settings?.healthProfile?.display}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Tap to configure alerts",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (isConfigured) {
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onToggleNotification
                )
            } else {
                IconButton(onClick = onConfigureClick) {
                    Icon(Icons.Default.Add, contentDescription = "Configure", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Add locations from the Home screen to enable monitoring.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}