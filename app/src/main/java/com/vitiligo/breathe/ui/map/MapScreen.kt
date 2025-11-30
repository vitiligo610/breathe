package com.vitiligo.breathe.ui.map

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.presentation.map.MapLibreContent
import com.vitiligo.breathe.presentation.shared.AqiCardMin
import org.maplibre.android.MapLibre
import org.maplibre.android.geometry.LatLng

@Composable
fun MapScreen(
    navigateToLocationDetailsPreview: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val cameraState = viewModel.cameraState
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
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
            },
            onMarkerClick = { aqi, lat, lng, _ ->
                viewModel.onMarkerSelected(aqi, lat, lng)
            },
            onMapClick = {
                viewModel.onMarkerDismissed()
            }
        )

        AnimatedVisibility(
            visible = state.selectedPoint == null,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.getCurrentLocation { loc ->
                        loc?.let {
                            viewModel.updateCameraState(it.latitude, it.longitude, 14.0)
                        } ?: Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "My Location"
                )
            }
        }

        AnimatedVisibility(
            visible = state.selectedPoint != null,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            state.selectedPoint?.let {
                AqiCardMin(
                    name = state.selectedPoint.city,
                    country = state.selectedPoint.country,
                    aqi = state.selectedPoint.aqi,
                    modifier = Modifier
                        .clickable {
                            navigateToLocationDetailsPreview(state.selectedPoint.latitude, state.selectedPoint.longitude)
                        }
                )
            }
        }
    }
}
