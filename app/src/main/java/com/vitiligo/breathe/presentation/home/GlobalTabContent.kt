package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.data.placeholder.placeholderLocationCardData
import com.vitiligo.breathe.domain.model.LocationCardData
import com.vitiligo.breathe.ui.theme.BreatheTheme

@Composable
fun GlobalTabContent(
    modifier: Modifier = Modifier,
    locations: List<LocationCardData> = placeholderLocationCardData
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(locations) { location ->
            LocationCard(data = location)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GlobalTabContentPreview() {
    BreatheTheme {
        GlobalTabContent()
    }
}