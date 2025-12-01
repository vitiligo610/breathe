package com.vitiligo.breathe.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.presentation.search.LocationSearchContent
import com.vitiligo.breathe.presentation.search.LocationSearchTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    navigateBack: () -> Unit,
    navigateToLocationDetailsPreview: (Double, Double, String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            LocationSearchTopBar(
                query = state.query,
                onQueryChange = viewModel::onQueryChange,
                onNavigateBack = navigateBack,
                focusRequester = focusRequester,
                navigateToLocation = { latitude, longitude ->
                    navigateToLocationDetailsPreview(latitude, longitude, "$latitude,$longitude")
                },
                getCurrentLocation = viewModel::getCurrentLocation
            )
        }
    ) { padding ->
        LocationSearchContent(
            state = state,
            onLocationSelected = { result ->
                navigateToLocationDetailsPreview(result.latitude, result.longitude, result.id)
            },
            modifier = Modifier.padding(padding)
        )
    }
}