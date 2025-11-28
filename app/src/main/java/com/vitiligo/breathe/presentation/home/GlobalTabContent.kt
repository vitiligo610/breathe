package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.data.placeholder.placeholderLocationCardData
import com.vitiligo.breathe.domain.model.ui.LocationCardData
import com.vitiligo.breathe.presentation.shared.RefreshBox
import com.vitiligo.breathe.ui.theme.BreatheTheme

@Composable
fun GlobalTabContent(
    navigateToLocationDetails: (Int) -> Unit,
    navigateToLocationSearch: () -> Unit,
    modifier: Modifier = Modifier,
    locations: List<LocationCardData> = placeholderLocationCardData,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = { },
) {
    RefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        if (locations.isEmpty()) {
            EmptyLocationList(
                onSearchLocationsClick = navigateToLocationSearch
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(0.dp)) }

                items(locations) { location ->
                    LocationCard(
                        data = location,
                        modifier = Modifier
                            .clickable { navigateToLocationDetails(location.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(0.dp)) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GlobalTabContentPreview() {
    BreatheTheme {
        GlobalTabContent(
            navigateToLocationDetails = { },
            navigateToLocationSearch = { }
        )
    }
}