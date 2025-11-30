package com.vitiligo.breathe.presentation.map

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.vitiligo.breathe.BuildConfig
import com.vitiligo.breathe.data.remote.model.MapLocationPoint
import com.vitiligo.breathe.domain.util.rememberMapViewWithLifecycle
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
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
fun MapLibreContent(
    mapPoints: List<MapLocationPoint>,
    cameraTarget: LatLng?,
    cameraZoom: Double,
    onCameraIdle: (LatLng?, Double, LatLngBounds) -> Unit,
    onMarkerClick: (Int, Double, Double, Boolean) -> Unit,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationIqKey = BuildConfig.LOCATION_IQ_API_KEY

    remember { MapLibre.getInstance(context) }

    val mapView = rememberMapViewWithLifecycle()
    var isMapInitialized by remember { mutableStateOf(false) }

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

    LaunchedEffect(cameraTarget, cameraZoom) {
        mapView.getMapAsync { map ->
            if (isMapInitialized && cameraTarget != null) {
                val currentTarget = map.cameraPosition.target

                val latDiff = kotlin.math.abs((currentTarget?.latitude ?: 0.0) - cameraTarget.latitude)
                val lngDiff = kotlin.math.abs((currentTarget?.longitude ?: 0.0) - cameraTarget.longitude)

                if (latDiff > 0.0001 || lngDiff > 0.0001) {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(cameraTarget, cameraZoom),
                        1000
                    )
                }
            }
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize(),
        update = { map ->
            map.getMapAsync { googleMap ->
                if (googleMap.style == null) {
                    val styleUrl =
                        "https://tiles.locationiq.com/v3/streets/vector.json?key=$locationIqKey"

                    googleMap.setStyle(styleUrl) { style ->
                        setupAqiLayers(style)

                        googleMap.cameraPosition = CameraPosition.Builder()
                            .target(cameraTarget)
                            .zoom(cameraZoom)
                            .build()

                        isMapInitialized = true

                        googleMap.addOnCameraIdleListener {
                            onCameraIdle(
                                googleMap.cameraPosition.target,
                                googleMap.cameraPosition.zoom,
                                googleMap.projection.visibleRegion.latLngBounds
                            )
                        }

                        googleMap.addOnMapClickListener { latLng ->
                            val screenPoint = googleMap.projection.toScreenLocation(latLng)
                            val features = googleMap.queryRenderedFeatures(screenPoint, AQI_CIRCLE_LAYER_ID)

                            if (features.isNotEmpty()) {
                                val feature = features[0]
                                onMarkerClick(
                                    feature.getNumberProperty("aqi").toInt(),
                                    latLng.latitude,
                                    latLng.longitude,
                                    feature.getBooleanProperty("is_cluster") ?: false
                                )
                                true
                            } else {
                                onMapClick()
                                false
                            }
                        }
                    }
                }
            }
        }
    )
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
                    literal("#ABD162"),
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
