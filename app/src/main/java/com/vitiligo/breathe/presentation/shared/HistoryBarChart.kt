package com.vitiligo.breathe.presentation.shared

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import com.vitiligo.breathe.domain.model.ui.HistoryViewMode
import com.vitiligo.breathe.domain.util.darken
import com.vitiligo.breathe.domain.util.getAQICategoryColor
import kotlin.math.ceil

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryBarChart(
    data: List<HistoryPoint>,
    selectedPoint: HistoryPoint,
    onPointSelected: (HistoryPoint) -> Unit,
    viewMode: HistoryViewMode,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f).toArgb()

    val textPaint = remember(density, labelColor) {
        Paint().apply {
            color = labelColor
            textSize = density.run { 10.sp.toPx() }
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 800))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(data) {
                    detectTapGestures { offset ->
                        handleTouch(
                            offset = offset,
                            canvasWidth = size.width.toFloat(),
                            data = data,
                            haptics = haptics,
                            onPointSelected = onPointSelected,
                            currentSelected = selectedPoint,
                            density = density,
                            isTap = true,
                            viewMode = viewMode
                        )
                    }
                }
                .pointerInput(data) {
                    detectHorizontalDragGestures { change, _ ->
                        handleTouch(
                            offset = change.position,
                            canvasWidth = size.width.toFloat(),
                            data = data,
                            haptics = haptics,
                            onPointSelected = onPointSelected,
                            currentSelected = selectedPoint,
                            density = density,
                            isTap = false,
                            viewMode = viewMode
                        )
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val isDaily = viewMode == HistoryViewMode.Daily
            val barWidthDp = if (isDaily) 7.dp else 5.dp
            val barGapDp = 1.dp

            val barWidthPx = density.run { barWidthDp.toPx() }
            val barGapPx = density.run { barGapDp.toPx() }
            val totalBarWidthPx = barWidthPx + barGapPx

            val bottomLabelHeight = 40f
            val chartAreaHeight = canvasHeight - bottomLabelHeight
            val chartStartPadding = 60f
            val availableChartWidth = canvasWidth - chartStartPadding

            val maxVisibleBars = (availableChartWidth / totalBarWidthPx).toInt()

            val visibleDataStartIndex = if (data.size > maxVisibleBars) {
                data.size - maxVisibleBars
            } else {
                0
            }
            val visibleData = data.subList(visibleDataStartIndex, data.size)

            val maxValue = visibleData.maxOfOrNull { it.value } ?: 100.0
            val yAxisMax = if (maxValue > 0) {
                (ceil(maxValue / 10.0) * 10.0).coerceAtLeast(10.0)
            } else {
                10.0
            }

            // Draw Grid
            val gridLines = 4
            val step = yAxisMax / gridLines
            for (i in 0..gridLines) {
                val value = step * i
                val y = chartAreaHeight - (value / yAxisMax * chartAreaHeight).toFloat()

                drawContext.canvas.nativeCanvas.drawText(
                    value.toInt().toString(),
                    10f,
                    y + 10f,
                    textPaint.apply { textAlign = Paint.Align.LEFT }
                )
            }

            drawLine(
                color = Color.LightGray,
                start = Offset(chartStartPadding - 10f, 0f),
                end = Offset(chartStartPadding - 10f, chartAreaHeight),
                strokeWidth = 2f
            )

            drawLine(
                color = Color.LightGray,
                start = Offset(chartStartPadding - 10f, chartAreaHeight),
                end = Offset(canvasWidth, chartAreaHeight),
                strokeWidth = 2f
            )

            visibleData.forEachIndexed { index, point ->
                val xOffset = chartStartPadding + (index * totalBarWidthPx)

                val barHeight = ((point.value / yAxisMax) * chartAreaHeight).toFloat() * animationProgress.value

                val baseColor = getAQICategoryColor(point.category)
                val isSelected = point == selectedPoint
                val finalColor = if (isSelected) baseColor.darken() else baseColor
                val yTop = chartAreaHeight - barHeight

                if (barHeight > 0) {
                    val topCornerRadius = 10f
                    val path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    offset = Offset(x = xOffset, y = yTop),
                                    size = Size(barWidthPx, barHeight)
                                ),
                                topLeft = CornerRadius(topCornerRadius, topCornerRadius),
                                topRight = CornerRadius(topCornerRadius, topCornerRadius),
                                bottomRight = CornerRadius(0f, 0f),
                                bottomLeft = CornerRadius(0f, 0f)
                            )
                        )
                    }
                    drawPath(path = path, color = finalColor)
                }

                val spacing = 6
                val isFirst = index == 0
                val isLast = index == visibleData.lastIndex
                val isInterval = index % spacing == 0
                val safeFromStart = index > 2
                val safeFromEnd = index < visibleData.lastIndex - 2
                val showLabel = isFirst || isLast || (isInterval && safeFromStart && safeFromEnd)

                if (showLabel) {
                    val labelText = if (isDaily) {
                        String.format("%02d", point.timestamp.dayOfMonth)
                    } else {
                        String.format("%02d:00", point.timestamp.hour)
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        labelText,
                        xOffset + (barWidthPx / 2),
                        canvasHeight - 10f,
                        textPaint.apply { textAlign = Paint.Align.CENTER }
                    )
                }
            }
        }
    }
}

private fun handleTouch(
    offset: Offset,
    canvasWidth: Float,
    data: List<HistoryPoint>,
    haptics: HapticFeedback,
    onPointSelected: (HistoryPoint) -> Unit,
    currentSelected: HistoryPoint,
    density: Density,
    isTap: Boolean,
    viewMode: HistoryViewMode
) {
    val isDaily = viewMode == HistoryViewMode.Daily
    val barWidthDp = if (isDaily) 7.dp else 5.dp
    val barGapDp = 1.dp

    val barWidthPx = with(density) { barWidthDp.toPx() }
    val barGapPx = with(density) { barGapDp.toPx() }
    val totalBarWidthPx = barWidthPx + barGapPx

    val chartStartPadding = 60f
    val availableChartWidth = canvasWidth - chartStartPadding

    val maxVisibleBars = (availableChartWidth / totalBarWidthPx).toInt()

    val visibleDataStartIndex = if (data.size > maxVisibleBars) {
        data.size - maxVisibleBars
    } else {
        0
    }

    val rawRelativeIndex = ((offset.x - chartStartPadding + 5) / totalBarWidthPx).toInt()
    val relativeIndex = rawRelativeIndex.coerceAtLeast(0)

    val actualIndex = visibleDataStartIndex + relativeIndex

    if (actualIndex in data.indices) {
        val selected = data[actualIndex]
        if (selected != currentSelected) {
            val feedbackType = if (isTap) HapticFeedbackType.LongPress else HapticFeedbackType.TextHandleMove
            haptics.performHapticFeedback(feedbackType)
            onPointSelected(selected)
        }
    }
}