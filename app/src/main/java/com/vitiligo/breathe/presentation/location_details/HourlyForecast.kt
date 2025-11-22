package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.R
import com.vitiligo.breathe.data.placeholder.mockHourlyForecastData
import com.vitiligo.breathe.domain.model.ui.HourlyForecastData
import com.vitiligo.breathe.domain.util.getWeatherIconRes
import com.vitiligo.breathe.presentation.shared.AqiValueBadge
import com.vitiligo.breathe.presentation.shared.DashedVerticalDivider
import com.vitiligo.breathe.presentation.shared.DetailBox

@Composable
fun HourlyForecast(
    modifier: Modifier = Modifier
) {
    DetailBox(
        label = "Hourly forecast",
        modifier = modifier
    ) {
        HourlyForecastContent()
    }
}

@Composable
private fun HourlyForecastContent(
    modifier: Modifier = Modifier,
    hourlyData: List<HourlyForecastData> = mockHourlyForecastData,
) {
    LazyRow(
        modifier = modifier
            .height(225.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        itemsIndexed(hourlyData) { index, data ->
            Row(
                modifier = Modifier
            ) {
                HourlyForecastItem(data = data)

                if (index < hourlyData.lastIndex) {
                    DashedVerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HourlyForecastItem(
    data: HourlyForecastData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = data.timeLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AqiValueBadge(value = data.aqi, category = data.aqiCategory)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Image(
                painter = painterResource(id = getWeatherIconRes(data.weatherIcon)),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "${data.tempC.toInt()}°",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wind_direction_solid_24),
                contentDescription = "Wind Direction",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(data.windSpeedDeg.toFloat()),
            )
            Text(
                text = "${data.windSpeedMph}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "mph",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_humidity_filled_24),
                contentDescription = "Humidity/Precipitation",
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = "${data.humidityPercent.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HourlyForecastPreview() {
    HourlyForecast()
}