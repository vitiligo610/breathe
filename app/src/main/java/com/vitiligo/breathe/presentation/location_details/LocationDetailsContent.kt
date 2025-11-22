package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.presentation.shared.AqiCard

@Composable
fun LocationDetailsContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        item {
            LocationDetailsHeading(
                title = "Karachi",
                localTime = "03:00"
            )
        }

        item { AqiCard() }

        item { HourlyForecast() }

        item { DailyForecast() }

        item { AirPollutants() }

        item { ReadingMetCard() }
    }
}
