package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.model.ui.HistoryViewMode
import com.vitiligo.breathe.domain.util.getAqiCategoryLabel
import com.vitiligo.breathe.domain.util.normalizeData
import com.vitiligo.breathe.presentation.shared.DetailBox
import com.vitiligo.breathe.presentation.shared.HistoryBarChart
import java.time.format.DateTimeFormatter

@Composable
fun History(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    DetailBox(
        label = "History",
        modifier = modifier
    ) {
        HistoryContent(
            data = state.chartData,
            onTabSelectionChange = { viewMode, option ->
                viewModel.setViewMode(viewMode)
                viewModel.setSelectedTab(option)
            },
            hourlyHistoryTabOptions = state.hourlyTabs,
            dailyHistoryTabOptions = state.dailyTabs
        )
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    data: List<HistoryPoint>,
    onTabSelectionChange: (HistoryViewMode, HistoryTabOption) -> Unit = { _, _ ->  },
    hourlyHistoryTabOptions: List<HistoryTabOption>,
    dailyHistoryTabOptions: List<HistoryTabOption>
) {
    var viewMode by rememberSaveable { mutableStateOf(HistoryViewMode.Hourly) }
    var selectedHourlyHistoryTabOption by rememberSaveable { mutableStateOf(if (hourlyHistoryTabOptions.isNotEmpty()) hourlyHistoryTabOptions[0] else HistoryTabOption.AQI ) }
    var selectedDailyHistoryTabOption by rememberSaveable { mutableStateOf(if (dailyHistoryTabOptions.isNotEmpty()) dailyHistoryTabOptions[0] else HistoryTabOption.AQI ) }

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
                        color = if (viewMode == HistoryViewMode.Hourly) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    viewMode = HistoryViewMode.Hourly
                    onTabSelectionChange(viewMode, hourlyHistoryTabOptions[0])
                },
                border = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 0.dp)
            )
            FilterChip(
                selected = viewMode == HistoryViewMode.Daily,
                label = {
                    Text(
                        text = "Daily",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        color = if (viewMode == HistoryViewMode.Daily) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    viewMode = HistoryViewMode.Daily
                    onTabSelectionChange(viewMode, dailyHistoryTabOptions[0])
                },
                border = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 0.dp)
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth(),
        )

        HistoryChart(
            data = data,
            viewMode = viewMode,
            hourlyHistoryTabOptions = hourlyHistoryTabOptions,
            dailyHistoryTabOptions = dailyHistoryTabOptions,
            selectedHourlyHistoryTabOption = selectedHourlyHistoryTabOption,
            selectedDailyHistoryTabOption = selectedDailyHistoryTabOption,
            onHourlyTabOptionChange = {
                selectedHourlyHistoryTabOption = it
                onTabSelectionChange(viewMode, it)
            },
            onDailyTabOptionChange = {
                selectedDailyHistoryTabOption = it
                onTabSelectionChange(viewMode, it)
            }
        )
    }
}

@Composable
fun HistoryChart(
    modifier: Modifier = Modifier,
    data: List<HistoryPoint>,
    viewMode: HistoryViewMode = HistoryViewMode.Hourly,
    hourlyHistoryTabOptions: List<HistoryTabOption>,
    dailyHistoryTabOptions: List<HistoryTabOption>,
    selectedHourlyHistoryTabOption: HistoryTabOption,
    selectedDailyHistoryTabOption: HistoryTabOption,
    onHourlyTabOptionChange: (HistoryTabOption) -> Unit,
    onDailyTabOptionChange: (HistoryTabOption) -> Unit
) {
    var selectedTabOption by remember { mutableStateOf(HistoryTabOption.AQI) }

    val normalizedData = remember(selectedTabOption, viewMode, data) {
        normalizeData(data, viewMode)
    }

    var selectedPoint by remember(normalizedData) {
        mutableStateOf(normalizedData.lastOrNull())
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {

        HistoryTabs(
            entries = if (viewMode == HistoryViewMode.Hourly) hourlyHistoryTabOptions else dailyHistoryTabOptions,
            selectedTab = if (viewMode == HistoryViewMode.Hourly) selectedHourlyHistoryTabOption else selectedDailyHistoryTabOption,
            onTabSelected = {
                selectedTabOption = it
                if (viewMode == HistoryViewMode.Hourly) {
                    onHourlyTabOptionChange(it)
                } else {
                    onDailyTabOptionChange(it)
                }
            }
        )

        if (normalizedData.isNotEmpty() && selectedPoint != null) {
            HistoryInfoHeader(
                point = selectedPoint!!,
                viewMode = viewMode,
                selectedTabOption = selectedTabOption
            )

            Spacer(Modifier.height(8.dp))

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
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        RoundedCornerShape(12.dp)
                    ),
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
    entries: List<HistoryTabOption>,
    selectedTab: HistoryTabOption,
    onTabSelected: (HistoryTabOption) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(entries) { tab ->
            val isSelected = tab == selectedTab

            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                },
                border = null
            )
        }
    }
}

@Composable
private fun HistoryInfoHeader(
    point: HistoryPoint,
    viewMode: HistoryViewMode,
    selectedTabOption: HistoryTabOption
) {
    val category = point.category

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
            .background(
                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
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
                    text = selectedTabOption.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = selectedTabOption.unit,
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
