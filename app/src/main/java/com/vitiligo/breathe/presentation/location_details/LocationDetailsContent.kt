package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.presentation.shared.AqiCard
import com.vitiligo.breathe.presentation.shared.RefreshBox
import com.vitiligo.breathe.presentation.shared.RightAlignedAddButton
import com.vitiligo.breathe.ui.location_details.LocationDetailsUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LocationDetailsContent(
    uiState: LocationDetailsUiState.Success,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
    onAddLocation: () -> Unit = { }
) {
    RefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
        ) {
            if (!uiState.data.locationAdded) {
                item {
                    RightAlignedAddButton(
                        onClick = onAddLocation
                    )
                }
            }

            item {
                LocationDetailsHeading(
                    title = uiState.data.locationName,
                    localTime = uiState.data.localTime
                )
            }

            item {
                AqiCard(data = uiState.data.aqiCardData)
            }

            item {
                HourlyForecast(data = uiState.data.hourlyForecasts)
            }

            item {
                DailyForecast(data = uiState.data.dailyForecasts)
            }

            item {
                AirPollutants(data = uiState.data.pollutants)
            }

            item {
                ReadingMetCard(threshold = uiState.data.aqiCardData.dominantPollutantValue)
            }

            item {
                HealthRecommendations(aqiCategory = uiState.data.aqiCardData.category)
            }

            item { History() }

            item {
                CommunityReports(
                    reports = uiState.data.nearbyReports
                )
            }
        }
    }
}
