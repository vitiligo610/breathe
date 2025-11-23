package com.vitiligo.breathe.presentation.location_details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.data.placeholder.mockDailyHistoryData
import com.vitiligo.breathe.data.placeholder.mockHourlyHistoryData
import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.util.HistoryViewMode
import com.vitiligo.breathe.domain.util.getAqiCategoryLabel
import com.vitiligo.breathe.domain.util.normalizeData
import com.vitiligo.breathe.presentation.shared.DetailBox
import com.vitiligo.breathe.presentation.shared.HistoryBarChart
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun History(
    modifier: Modifier = Modifier
) {
    DetailBox(
        label = "History",
        modifier = modifier
    ) {
        HistoryContent()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier
) {
    var viewMode by rememberSaveable { mutableStateOf(HistoryViewMode.Hourly) }

    val data = if (viewMode == HistoryViewMode.Hourly) mockHourlyHistoryData else mockDailyHistoryData

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = viewMode == HistoryViewMode.Hourly,
                label = {
                    Text(
                        text = "Hourly",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = { viewMode = HistoryViewMode.Hourly },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = viewMode == HistoryViewMode.Daily,
                label = {
                    Text(
                        text = "Daily",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = { viewMode = HistoryViewMode.Daily },
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        HistoryChart(
            data = data,
            viewMode = viewMode
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryChart(
    modifier: Modifier = Modifier,
    data: List<HistoryPoint> = mockHourlyHistoryData,
    viewMode: HistoryViewMode // Receive mode
) {
    var selectedTab by remember { mutableStateOf(HistoryTabOption.AQI) }

    val normalizedData = remember(selectedTab, viewMode, data) {
        val filtered = data.filter { it.type == selectedTab }
        normalizeData(filtered, viewMode)
    }

    var selectedPoint by remember(normalizedData) {
        mutableStateOf(normalizedData.lastOrNull())
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {

//        HistoryTabs(
//            selectedTab = selectedTab,
//            onTabSelected = { selectedTab = it }
//        )

        if (normalizedData.isNotEmpty() && selectedPoint != null) {
            HistoryInfoHeader(
                point = selectedPoint!!,
                viewMode = viewMode
            )

            HistoryBarChart(
                data = normalizedData,
                selectedPoint = selectedPoint!!,
                viewMode = viewMode,
                onPointSelected = { point ->
                    selectedPoint = point
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No data to plot",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HistoryTabs(
    selectedTab: HistoryTabOption,
    onTabSelected: (HistoryTabOption) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HistoryTabOption.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryInfoHeader(
    point: HistoryPoint,
    viewMode: HistoryViewMode
) {
    val category = point.getCategory()

    val dateText = if (viewMode == HistoryViewMode.Daily) {
        val dailyFormatter = DateTimeFormatter.ofPattern("EEE, MMMM dd")
        point.timestamp.format(dailyFormatter)
    } else {
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val endTime = point.timestamp.plusHours(1)
        "${point.timestamp.format(dateFormatter)}, ${point.timestamp.format(timeFormatter)}-${endTime.format(timeFormatter)}"
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = dateText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = point.type.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = point.type.unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getAqiCategoryLabel(category),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = point.value.toInt().toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun HourlyHistoryPreview() {
    HistoryContent()
}