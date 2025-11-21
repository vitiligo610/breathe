package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.presentation.shared.AqiCard

@Composable
fun LocationDetailsContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        LocationDetailsHeading(
            title = "Karachi",
            localTime = "03:00"
        )
        Spacer(modifier = Modifier.height(12.dp))
        AqiCard()
        Spacer(modifier = Modifier.height(12.dp))
    }
}