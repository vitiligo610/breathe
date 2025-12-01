package com.vitiligo.breathe.ui.map

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.presentation.map.MapLibreContent
import com.vitiligo.breathe.presentation.map.MapSearchBar
import com.vitiligo.breathe.presentation.map.ReportingModeControls
import com.vitiligo.breathe.presentation.map.StandardMapControls
import org.maplibre.android.geometry.LatLng

@Composable
fun MapScreen(
    navigateToLocationDetailsPreview: (Double, Double) -> Unit,
    navigateToCommunityReport: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.value
    val searchState = viewModel.searchState.value
    val cameraState = viewModel.cameraState

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isReportingMode by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        MapLibreContent(
            mapPoints = state.mapPoints,
            cameraTarget = LatLng(cameraState.latitude, cameraState.longitude),
            cameraZoom = cameraState.zoom,
            onCameraIdle = { target, zoom, bounds ->
                viewModel.updateCameraState(target?.latitude, target?.longitude, zoom)
                viewModel.onCameraIdle(
                    bounds.latitudeSouth, bounds.longitudeWest,
                    bounds.latitudeNorth, bounds.longitudeEast,
                    zoom
                )
                keyboardController?.hide()
            },
            onMarkerClick = { aqi, lat, lng, _ ->
                if (!isReportingMode) {
                    viewModel.onMarkerSelected(aqi, lat, lng)
                    keyboardController?.hide()
                }
            },
            onMapClick = {
                viewModel.onMarkerDismissed()
                keyboardController?.hide()
            }
        )

        AnimatedVisibility(
            visible = !isReportingMode,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            MapSearchBar(
                query = searchState.query,
                onQueryChange = viewModel::onSearchQueryChange,
                results = searchState.results,
                onResultClick = { result ->
                    viewModel.onSearchResultSelected(result)
                    keyboardController?.hide()
                },
                onToggle = { isActive ->
                    if (!isActive) keyboardController?.hide()
                    else viewModel.onMarkerDismissed()
                }
            )
        }

        AnimatedVisibility(
            visible = isReportingMode,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Center Target",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        AnimatedVisibility(
            visible = isReportingMode,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Move map to pinpoint location",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = !isReportingMode,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                StandardMapControls(
                    selectedPoint = state.selectedPoint,
                    onNavigateToReport = { isReportingMode = true },
                    onNavigateToDetails = navigateToLocationDetailsPreview,
                    onMyLocationClick = {
                        viewModel.getCurrentLocation { loc ->
                            loc?.let {
                                viewModel.updateCameraState(it.latitude, it.longitude, 14.0)
                            } ?: Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = isReportingMode,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                ReportingModeControls(
                    onCancel = { isReportingMode = false },
                    onConfirm = {
                        navigateToCommunityReport(cameraState.latitude, cameraState.longitude)
                        isReportingMode = false
                    }
                )
            }
        }
    }
}