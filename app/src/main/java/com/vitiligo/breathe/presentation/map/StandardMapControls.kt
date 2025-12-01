package com.vitiligo.breathe.presentation.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.presentation.shared.AqiCardMin
import com.vitiligo.breathe.ui.map.SelectedPointInfo

@Composable
fun StandardMapControls(
    selectedMarkerType: String,
    selectedPoint: SelectedPointInfo?,
    onNavigateToReport: () -> Unit,
    onNavigateToDetails: (Double, Double) -> Unit,
    onMyLocationClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = onNavigateToReport,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.AddAlert,
                    contentDescription = "Report Pollution"
                )
            }

            FloatingActionButton(
                onClick = onMyLocationClick,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location"
                )
            }
        }

        if (selectedPoint != null) {
            Spacer(modifier = Modifier.height(16.dp))
            if (selectedMarkerType == "aqi" && selectedPoint.aqi != null) {
                AqiCardMin(
                    name = selectedPoint.city,
                    country = selectedPoint.country,
                    aqi = selectedPoint.aqi,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToDetails(selectedPoint.latitude, selectedPoint.longitude)
                        }
                )
            }
            if (selectedMarkerType == "pollution_report" ) {
                PollutionReportCard(
                    type = selectedPoint.reportType ?: ReportType.OTHER,
                    description = selectedPoint.reportDescription ?: "No description",
                    time = selectedPoint.reportedAt ?: 0L,
                    locationName = "${selectedPoint.city}, ${selectedPoint.country}",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
