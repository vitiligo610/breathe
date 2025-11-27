package com.vitiligo.breathe.ui.location_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.presentation.location_details.LocationDetailsContent
import com.vitiligo.breathe.presentation.location_details.LocationDetailsTopBar
import com.vitiligo.breathe.presentation.location_details.StickyLocationHeader
import com.vitiligo.breathe.presentation.shared.ErrorState
import com.vitiligo.breathe.presentation.shared.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationDetailsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val uiState by viewModel.state.collectAsState()

    val showStickyDetails by remember {
        derivedStateOf {
            val thresholdPx = with(density) { 500.dp.toPx() }
            lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > thresholdPx
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                LocationDetailsTopBar(
                    navigateBack = navigateBack,
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            when (uiState) {
                is LocationDetailsUiState.Loading -> LoadingState()
                is LocationDetailsUiState.Error -> ErrorState(
                    errorMessage = (uiState as LocationDetailsUiState.Error).message,
                    retry = viewModel::refresh
                )
                is LocationDetailsUiState.Success -> LocationDetailsContent(
                    uiState = (uiState as LocationDetailsUiState.Success),
                    lazyListState = lazyListState,
                    isRefreshing = (uiState as LocationDetailsUiState.Success).isRefreshing,
                    onRefresh = viewModel::refresh,
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        if (showStickyDetails && uiState is LocationDetailsUiState.Success) {
            val successState = uiState as LocationDetailsUiState.Success

            StickyLocationHeader(
                locationName = successState.data.locationName,
                aqiScore = successState.data.aqiCardData.aqi,
                dominantPollutant = successState.data.aqiCardData.dominantPollutant,
                dominantPollutantValue = successState.data.aqiCardData.dominantPollutantValue,
                dominantPollutantUnit = successState.data.aqiCardData.dominantPollutantUnit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 50.dp)
            )
        }
    }
}
