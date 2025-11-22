package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
        modifier = modifier
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LocationDetailsHeading(
                    title = "Karachi",
                    localTime = "03:00"
                )
                AqiCard()
                HourlyForecast()
                DailyForecast()
            }
        }
    }
}
