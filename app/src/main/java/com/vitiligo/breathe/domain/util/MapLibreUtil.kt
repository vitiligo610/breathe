package com.vitiligo.breathe.domain.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import android.graphics.Canvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.presentation.shared.getReportColor
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.vitiligo.breathe.R
import com.vitiligo.breathe.presentation.shared.getDrawableIdForReport

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle, mapView) {
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }

suspend inline fun MapView.awaitMap(): MapLibreMap =
    suspendCoroutine { continuation ->
        getMapAsync {
            continuation.resume(it)
        }
    }

@Composable
fun rememberPollutionBitmaps(context: Context): Map<String, Bitmap> {
    return remember {
        ReportType.entries.associate { type ->
            val color = getReportColor(type)

            val drawableId = getDrawableIdForReport(type)

            val drawable = ContextCompat.getDrawable(context, drawableId)
                ?.mutate()
                ?: ContextCompat.getDrawable(context, R.drawable.ic_humidity_filled_24)!!.mutate()

            DrawableCompat.setTint(drawable, color.toArgb())
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)

            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            "icon-${type.name}" to bitmap
        }
    }
}