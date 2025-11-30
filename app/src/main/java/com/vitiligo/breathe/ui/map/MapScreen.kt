package com.vitiligo.breathe.ui.map

import android.graphics.Color
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vitiligo.breathe.BuildConfig
import com.vitiligo.breathe.domain.util.awaitMap
import com.vitiligo.breathe.domain.util.rememberMapViewWithLifecycle
import com.vitiligo.breathe.presentation.shared.AqiCardMin
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression.get
import org.maplibre.android.style.expressions.Expression.literal
import org.maplibre.android.style.expressions.Expression.step
import org.maplibre.android.style.expressions.Expression.stop
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.Property.TEXT_ANCHOR_CENTER
import org.maplibre.android.style.layers.PropertyFactory.circleColor
import org.maplibre.android.style.layers.PropertyFactory.circleRadius
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth
import org.maplibre.android.style.layers.PropertyFactory.textAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.textAnchor
import org.maplibre.android.style.layers.PropertyFactory.textColor
import org.maplibre.android.style.layers.PropertyFactory.textField
import org.maplibre.android.style.layers.PropertyFactory.textIgnorePlacement
import org.maplibre.android.style.layers.PropertyFactory.textSize
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

private const val AQI_SOURCE_ID = "aqi-backend-source"
private const val AQI_CIRCLE_LAYER_ID = "aqi-circle-layer"
private const val AQI_TEXT_LAYER_ID = "aqi-text-layer"

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val cameraState = viewModel.cameraState

    val context = LocalContext.current
    val locationIqKey = BuildConfig.LOCATION_IQ_API_KEY

    remember { MapLibre.getInstance(context) }
    val mapView = rememberMapViewWithLifecycle()
    var isCameraRestored by remember { mutableStateOf(false) }

    val mapPoints = state.mapPoints
    LaunchedEffect(mapPoints) {
        mapView.getMapAsync { map ->
            map.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>(AQI_SOURCE_ID)
                if (source != null) {
                    val features = mapPoints.map { point ->
                        Feature.fromGeometry(
                            Point.fromLngLat(point.longitude, point.latitude)
                        ).apply {
                            addNumberProperty("aqi", point.aqi)
                            addBooleanProperty("is_cluster", point.isCluster)
                            addNumberProperty("point_count", point.pointCount)

                            val idHash = "${point.latitude},${point.longitude}".hashCode()
                            addNumberProperty("id", idHash)
                        }
                    }
                    source.setGeoJson(FeatureCollection.fromFeatures(features))
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { map ->
                map.getMapAsync { googleMap ->
                    if (googleMap.style == null) {
                        val styleUrl =
                            "https://tiles.locationiq.com/v3/streets/vector.json?key=$locationIqKey"

                        googleMap.setStyle(styleUrl) { style ->
                            setupAqiLayers(style)

                            if (!isCameraRestored) {
                                googleMap.cameraPosition = CameraPosition.Builder()
                                    .target(LatLng(cameraState.latitude, cameraState.longitude))
                                    .zoom(cameraState.zoom)
                                    .build()

                                isCameraRestored = true
                            }

                            googleMap.addOnCameraIdleListener {
                                val position = googleMap.cameraPosition
                                val bounds = googleMap.projection.visibleRegion.latLngBounds

                                viewModel.updateCameraState(position.target?.latitude,
                                    position.target?.longitude, position.zoom)

                                viewModel.onCameraIdle(
                                    bounds.latitudeSouth, bounds.longitudeWest,
                                    bounds.latitudeNorth, bounds.longitudeEast,
                                    position.zoom
                                )
                            }

                            googleMap.addOnMapClickListener { latLng ->
                                val screenPoint = googleMap.projection.toScreenLocation(latLng)
                                val features =
                                    googleMap.queryRenderedFeatures(
                                        screenPoint,
                                        AQI_CIRCLE_LAYER_ID
                                    )

                                if (features.isNotEmpty()) {
                                    val feature = features[0]

                                    viewModel.onMarkerSelected(
                                        feature.getNumberProperty("aqi").toInt(),
                                        latLng.latitude, latLng.longitude
                                    )

                                    true
                                } else {
                                    viewModel.onMarkerDismissed()

                                    false
                                }
                            }
                        }
                    }
                }
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
                    viewModel.getCurrentLocation { userLatLng ->
                        if (userLatLng != null) {
                            mapView.awaitMap().animateCamera(
                                CameraUpdateFactory.newLatLngZoom(userLatLng, 14.0)
                            )
                        } else {
                            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                        }
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
                    aqi = state.selectedPoint.aqi
                )
            }
        }
    }
}

private fun setupAqiLayers(style: Style) {
    if (style.getSource(AQI_SOURCE_ID) == null) {
        style.addSource(GeoJsonSource(AQI_SOURCE_ID))

        val circleLayer = CircleLayer(AQI_CIRCLE_LAYER_ID, AQI_SOURCE_ID)
        circleLayer.setProperties(
            circleStrokeColor(Color.WHITE),
            circleStrokeWidth(1f),
            circleRadius(18f),

            circleColor(
                step(get("aqi"),
                    literal("#ABD162"), // 0-50 (Green)
                    stop(51, literal("#F7D45F")),
                    stop(101, literal("#FC9957")),
                    stop(151, literal("#F7666C")),
                    stop(201, literal("#A37DB8")),
                    stop(301, literal("#A17584"))
                )
            )
        )
        style.addLayer(circleLayer)

        val textLayer = SymbolLayer(AQI_TEXT_LAYER_ID, AQI_SOURCE_ID)
        textLayer.setProperties(
            textField(
                get("aqi")
            ),
            textSize(13f),

            textColor(
                step(get("aqi"),
                    literal("black"),
                    stop(101, literal("white"))
                )
            ),

            textIgnorePlacement(true),
            textAllowOverlap(true),
            textAnchor(TEXT_ANCHOR_CENTER)
        )

        style.addLayer(textLayer)
    }
}