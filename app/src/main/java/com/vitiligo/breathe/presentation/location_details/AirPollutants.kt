package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.data.placeholder.mockAirPollutantsData
import com.vitiligo.breathe.domain.model.Pollutant
import com.vitiligo.breathe.domain.util.getPollutantDetails
import com.vitiligo.breathe.presentation.shared.AqiCategoryIndicator
import com.vitiligo.breathe.presentation.shared.DetailBox

@Composable
fun AirPollutants(
    modifier: Modifier = Modifier,
    data: List<Pollutant> = mockAirPollutantsData
) {
    DetailBox(
        label = "Air Pollutants",
        modifier = modifier
    ) {
        AirPollutantsContent(pollutants = data)
    }
}

@Composable
private fun AirPollutantsContent(
    modifier: Modifier = Modifier,
    pollutants: List<Pollutant>
) {
    val rowCount = (pollutants.size + 1) / 2
    val gridHeight = (rowCount * 86 + 0.coerceAtLeast(rowCount - 1) * 8).dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .height(gridHeight)
    ) {
        items(pollutants) {
            AirPollutantCard(
                pollutant = it,
            )
        }
    }
}

@Composable
private fun AirPollutantCard(
    pollutant: Pollutant,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f))
            .padding(12.dp)
    ) {
        Text(
            text = pollutant.displayValue,
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = pollutant.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Light
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(top = 2.dp)
        ) {
            AqiCategoryIndicator(
                aqiCategory = pollutant.category,
                modifier = Modifier
                    .padding(end = 2.dp)
            )
            Text(
                text = pollutant.reading.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = pollutant.unit,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PollutantsGrid(
    pollutants: List<Pollutant>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        pollutants.chunked(2).forEach { rowPollutants ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                rowPollutants.forEach { pollutant ->
                    AirPollutantCard(
                        pollutant = pollutant,
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                if (rowPollutants.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AirPollutantCardPreview() {
    AirPollutantCard(
        getPollutantDetails(
            key = "co",
            reading = 0.2
        )
    )
}
