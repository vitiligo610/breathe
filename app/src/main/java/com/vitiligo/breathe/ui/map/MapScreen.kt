package com.vitiligo.breathe.ui.map

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vitiligo.breathe.BuildConfig
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.RasterLayer
import org.maplibre.android.style.sources.RasterSource
import org.maplibre.android.style.sources.TileSet

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val waqiToken = BuildConfig.WAQI_API_KEY

    val locationIqKey = BuildConfig.LOCATION_IQ_API_KEY
    val baseMapStyle = "https://tiles.locationiq.com/v3/streets/vector.json?key=$locationIqKey"

    remember { MapLibre.getInstance(context) }

    val mapView = remember { MapView(context) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = { map ->
            map.getMapAsync { googleMap ->

                // 1. Load the Base Map (Streets)
                googleMap.setStyle(baseMapStyle) { style ->

                    // 2. Add WAQI Raster Tile Overlay
                    addWaqiLayer(style, waqiToken)

                    // Initial Camera
                    if (googleMap.cameraPosition.target?.latitude == 0.0) {
                        googleMap.cameraPosition = CameraPosition.Builder()
                            .target(LatLng(24.8607, 67.0011))
                            .zoom(8.0)
                            .build()
                    }

                    // 3. Handle Clicks
                    // Since tiles are images, we cannot query "features".
                    // We must calculate the click lat/lng and ask the API for the nearest station.
                    googleMap.addOnMapClickListener { latLng ->
                        Toast.makeText(context, "Clicked: ${latLng.latitude}, ${latLng.longitude}", Toast.LENGTH_SHORT).show()

                        // To implement "Click", you would make a network call here:
                        // GET https://api.waqicn.org/feed/geo:${latLng.latitude};${latLng.longitude}/?token=...
                        // This returns the exact station details for that coordinate.
                        true
                    }
                }
            }
        }
    )
}

/**
 * Adds the WAQI Raster Tile layer on top of the base map.
 */
private fun addWaqiLayer(style: Style, token: String) {
    val sourceId = "waqi-raster-source"
    val layerId = "waqi-raster-layer"

    if (style.getSource(sourceId) == null) {
        val tileUrl = "https://tiles.aqicn.org/tiles/usepa-aqi/{z}/{x}/{y}.svg?token=$token"

        // 1. Define the Raster Source
        val rasterSource = RasterSource(
            sourceId,
            TileSet(
                "2.1.0",
                tileUrl
            ),
            256
        )
        style.addSource(rasterSource)

        // 2. Define the Raster Layer
        val rasterLayer = RasterLayer(layerId, sourceId)

        // Ensure the AQI dots are drawn ON TOP of the map labels/roads
        style.addLayer(rasterLayer)
    }
}
