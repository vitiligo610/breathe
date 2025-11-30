package com.vitiligo.breathe.ui.map

import android.graphics.Color
import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vitiligo.breathe.BuildConfig
import com.vitiligo.breathe.presentation.shared.AqiCardMin
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
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

private const val AQI_SOURCE_ID = "aqi-backend-source"
private const val AQI_CIRCLE_LAYER_ID = "aqi-circle-layer"
private const val AQI_TEXT_LAYER_ID = "aqi-text-layer"

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val locationIqKey = BuildConfig.LOCATION_IQ_API_KEY

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
                            // Inject properties for the Style Expressions
                            addNumberProperty("aqi", point.aqi)
                            addBooleanProperty("is_cluster", point.isCluster)
                            addNumberProperty("point_count", point.pointCount)

                            // Generate a simple ID hash for click handling since API doesn't give one
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
                    val styleUrl =
                        "https://tiles.locationiq.com/v3/streets/vector.json?key=$locationIqKey"

                    googleMap.setStyle(styleUrl) { style ->
                        setupAqiLayers(style)

                        if (googleMap.cameraPosition.target?.latitude == 0.0) {
                            googleMap.cameraPosition = CameraPosition.Builder()
                                .target(LatLng(24.8607, 67.0011))
                                .zoom(10.0)
                                .build()
                        }

                        googleMap.addOnCameraIdleListener {
                            val bounds = googleMap.projection.visibleRegion.latLngBounds
                            val zoom = googleMap.cameraPosition.zoom

                            viewModel.onCameraIdle(
                                swLat = bounds.latitudeSouth,
                                swLon = bounds.longitudeWest,
                                neLat = bounds.latitudeNorth,
                                neLon = bounds.longitudeEast,
                                zoom = zoom
                            )
                        }

                        googleMap.addOnMapClickListener { latLng ->
                            val screenPoint = googleMap.projection.toScreenLocation(latLng)
                            val features =
                                googleMap.queryRenderedFeatures(screenPoint, AQI_CIRCLE_LAYER_ID)

                            if (features.isNotEmpty()) {
                                val feature = features[0]
                                val isCluster = feature.getBooleanProperty("is_cluster") ?: false

                                viewModel.onMarkerSelected(
                                    feature.getNumberProperty("aqi").toInt(),
                                    latLng.latitude, latLng.longitude
                                )
//                                if (isCluster) {
//                                    val currentZoom = googleMap.cameraPosition.zoom
//                                    googleMap.animateCamera(
//                                        CameraUpdateFactory.newLatLngZoom(
//                                            latLng,
//                                            currentZoom + 2
//                                        )
//                                    )
//                                } else {
//                                    viewModel.onMarkerSelected(feature.getNumberProperty("aqi") as Int, latLng.latitude, latLng.longitude)
//                                }
                                true
                            } else {
                                viewModel.onMarkerDismissed()

                                false
                            }
                        }
                    }
                }
            }
        )

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

//            circleRadius(
//                switchCase(
//                    toBool(get("is_cluster")),
//                    // Clusters are slightly larger
//                    interpolate(exponential(2.5f), zoom(), stop(2, 100f), stop(12, 108f)),
//                    // Single points are standard size
//                    interpolate(exponential(2.5f), zoom(), stop(2, 38f), stop(98, 100f))
//                )
//            ),

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
//                switchCase(
//                    toBool(get("is_cluster")),
//                    toString(get("point_count")),
//                    toString(get("aqi"))
//                )
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