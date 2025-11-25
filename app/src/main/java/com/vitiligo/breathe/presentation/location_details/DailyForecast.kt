package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.R
import com.vitiligo.breathe.data.placeholder.mockDailyForecastData
import com.vitiligo.breathe.domain.model.ui.ForecastDayDataExtended
import com.vitiligo.breathe.domain.util.getWeatherIconRes
import com.vitiligo.breathe.presentation.shared.AqiValueBadge
import com.vitiligo.breathe.presentation.shared.DetailBox

@Composable
fun DailyForecast(
    modifier: Modifier = Modifier
) {
    var selectedCount by rememberSaveable { mutableIntStateOf(3) }

    DetailBox(
        label = "Daily forecast",
        headerContent = {
            DaysSelector(
                selectedCount = selectedCount,
                onCountSelect = { selectedCount = it }
            )
        },
        modifier = modifier
    ) {
       DailyForecastContent(
           selectedCount = selectedCount,
           modifier = Modifier
               .padding(vertical = 6.dp)
       )
    }
}

@Composable
private fun DailyForecastContent(
    modifier: Modifier = Modifier,
    selectedCount: Int = 3,
    data: List<ForecastDayDataExtended> = mockDailyForecastData
) {
    val items = data.take(selectedCount)
    val rowHeight = 28.dp

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(22.dp),
        modifier = modifier
    ) {
        item {
            CardColumn {
                items.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .height(rowHeight)
                    ) {
                        Text(
                            text = it.dayLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .width(50.dp)
                        )
                        AqiValueBadge(value = it.aqi, category = it.aqiCategory)
                    }
                }
            }
        }

        item {
            CardColumn {
                items.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .height(rowHeight)
                    ) {
                        Image(
                            painter = painterResource(id = getWeatherIconRes(it.weatherCode)),
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                        )
                        Text(
                            text = "${it.highTempC.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .width(30.dp)
                        )
                        Text(
                            text = "${it.lowTempC.toInt()}°",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier
                                .width(30.dp)
                        )
                    }
                }
            }
        }

        item {
            CardColumn {
                items.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .height(rowHeight)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_wind_direction_solid_24),
                            contentDescription = "Wind Direction",
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(it.windSpeedDeg.toFloat()),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            modifier = Modifier
                                .width(60.dp)
                        ) {
                            Text(
                                text = "${it.windSpeedKph}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "mph",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item {
            CardColumn {
                items.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .height(rowHeight)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_humidity_filled_24),
                            contentDescription = "Humidity/Precipitation",
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = "${it.humidityPercent.toInt()}%",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardColumn(
    modifier: Modifier = Modifier,
    verticalGap: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(verticalGap),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
private fun DaysSelector(
    selectedCount: Int,
    onCountSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        Text(
            text = "3d",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selectedCount == 3) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier
                .clickable { onCountSelect(3) }
        )
        Text(
            text = "|",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "7d",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selectedCount == 7) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier
                .clickable { onCountSelect(7) }
        )
    }
}
