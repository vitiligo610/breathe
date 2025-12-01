package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.presentation.shared.ErrorState
import com.vitiligo.breathe.presentation.shared.LoadingState
import com.vitiligo.breathe.ui.home.HomeUiState
import com.vitiligo.breathe.ui.home.HomeViewModel

@Composable
fun HomeContent(
    navigateToLocationDetails: (Int) -> Unit,
    navigateToLocationSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        when (state) {
            is HomeUiState.Loading -> LoadingState()
            is HomeUiState.Error -> ErrorState(
                errorMessage = (state as HomeUiState.Error).message
            )
            is HomeUiState.Success -> {
                GlobalTabContent(
                    navigateToLocationDetails = navigateToLocationDetails,
                    navigateToLocationSearch = navigateToLocationSearch,
                    locations = (state as HomeUiState.Success).locations,
                    onRefresh = viewModel::refreshData,
                    isRefreshing = (state as HomeUiState.Success).isRefreshing
                )
            }
        }

    }
}
