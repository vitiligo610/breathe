package com.vitiligo.breathe.presentation.location_details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.util.getHealthRecommendations
import com.vitiligo.breathe.presentation.shared.DetailBox

@Composable
fun HealthRecommendations(
    modifier: Modifier = Modifier
) {
    DetailBox(
        label = "Health recommendations",
        modifier = modifier
    ) {
        HealthRecommendationsContent()
    }
}

@Composable
private fun HealthRecommendationsContent(
    modifier: Modifier = Modifier,
    aqiCategory: AqiCategory = AqiCategory.ORANGE
) {
    val recs = getHealthRecommendations(category = aqiCategory)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        HealthRecommendationItem(
            text = recs.outdoorRec,
            icon = recs.outdoorRes
        )
        HealthRecommendationItem(
            text = recs.windowsRec,
            icon = recs.windowsRes
        )
        if (aqiCategory != AqiCategory.GREEN) {
            HealthRecommendationItem(
                text = recs.maskRec,
                icon = recs.maskRes
            )
            HealthRecommendationItem(
                text = recs.purifierRec,
                icon = recs.purifierRes
            )
        }
    }
}

@Composable
private fun HealthRecommendationItem(
    text: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier
                .size(58.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}