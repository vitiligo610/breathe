package com.vitiligo.breathe.presentation.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitiligo.breathe.domain.model.ui.HealthZoneUiModel
import com.vitiligo.breathe.ui.health.HealthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthContent(
    modifier: Modifier = Modifier,
    viewModel: HealthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedZoneForEdit by remember { mutableStateOf<HealthZoneUiModel?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LazyColumn(
        modifier = modifier
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