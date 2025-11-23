package com.vitiligo.breathe.presentation.location_details

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.breathe.data.placeholder.mockHourlyHistoryData
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.model.ui.HourlyHistoryPoint
import com.vitiligo.breathe.domain.util.darken
import com.vitiligo.breathe.domain.util.getAQICategoryColor
import com.vitiligo.breathe.domain.util.getAqiCategoryLabel
import com.vitiligo.breathe.presentation.shared.DetailBox
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun History(
    modifier: Modifier = Modifier
) {
    DetailBox(
        label = "History",
        modifier = modifier
    ) {
        HistoryContent()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier
) {
    var selectedOption by rememberSaveable { mutableStateOf("Hourly") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedOption == "Hourly",
                label = {
                    Text(
                        text = "Hourly",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                onClick = { selectedOption = "Hourly" },
                modifier = Modifier
                    .weight(1f)
            )
            FilterChip(
                selected = selectedOption == "Daily",
                label = {
                    Text(
                        text = "Daily",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                onClick = { selectedOption = "Daily" },
                modifier = Modifier
                    .weight(1f)
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        HourlyHistory()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyHistory(
    modifier: Modifier = Modifier,
    data: List<HourlyHistoryPoint> = mockHourlyHistoryData
) {
    var selectedTab by remember { mutableStateOf(HistoryTabOption.AQI) }

    val normalizedData = remember(selectedTab, data) {
        val filtered = data.filter { it.type == selectedTab }
        normalizeData(filtered)
    }

    var selectedPoint by remember(normalizedData) {
        mutableStateOf(normalizedData.lastOrNull())
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

//        HistoryTabs(
//            selectedTab = selectedTab,
//            onTabSelected = { selectedTab = it }
//        )

        if (normalizedData.isNotEmpty() && selectedPoint != null) {
            HistoryInfoHeader(
                point = selectedPoint!!
            )

            HistoryBarChart(
                data = normalizedData,
                selectedPoint = selectedPoint!!,
                onPointSelected = { point ->
                    selectedPoint = point
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No data to plot",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HistoryTabs(
    selectedTab: HistoryTabOption,
    onTabSelected: (HistoryTabOption) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HistoryTabOption.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryInfoHeader(
    point: HourlyHistoryPoint
) {
    val category = point.getCategory()
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val endTime = point.timestamp.plusHours(1)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "${point.timestamp.format(dateFormatter)}, ${point.timestamp.format(timeFormatter)}-${endTime.format(timeFormatter)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = point.type.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = point.type.unit,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = getAqiCategoryLabel(category),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = point.value.toInt().toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryBarChart(
    data: List<HourlyHistoryPoint>,
    selectedPoint: HourlyHistoryPoint,
    onPointSelected: (HourlyHistoryPoint) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

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
                            isTap = true
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
                            isTap = false
                        )
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val barWidthPx = density.run { 5.dp.toPx() }
            val barGapPx = density.run { 1.dp.toPx() }
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

            val gridLines = 4
            val step = yAxisMax / gridLines
            for (i in 0..gridLines) {
                val value = step * i
                val y = chartAreaHeight - (value / yAxisMax * chartAreaHeight).toFloat()

                drawContext.canvas.nativeCanvas.drawText(
                    value.toInt().toString(),
                    10f,
                    y + 10f, // vertical alignment adjustment
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

                val category = point.getCategory()
                val baseColor = getAQICategoryColor(category)

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

                    drawPath(
                        path = path,
                        color = finalColor
                    )
                }

                val spacing = 6

                val isFirst = index == 0
                val isLast = index == visibleData.lastIndex

                val isInterval = index % spacing == 0

                val safeFromStart = index > 2
                val safeFromEnd = index < visibleData.lastIndex - 2

                val showLabel = isFirst || isLast || (isInterval && safeFromStart && safeFromEnd)

                if (showLabel) {
                    drawContext.canvas.nativeCanvas.drawText(
                        String.format("%02d:00", point.timestamp.hour),
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
    data: List<HourlyHistoryPoint>,
    haptics: HapticFeedback,
    onPointSelected: (HourlyHistoryPoint) -> Unit,
    currentSelected: HourlyHistoryPoint,
    density: Density,
    isTap: Boolean
) {
    val barWidthPx = with(density) { 5.dp.toPx() }
    val barGapPx = with(density) { 1.dp.toPx() }
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

@RequiresApi(Build.VERSION_CODES.O)
private fun normalizeData(data: List<HourlyHistoryPoint>): List<HourlyHistoryPoint> {
    if (data.isEmpty()) return emptyList()

    val sorted = data.sortedBy { it.timestamp }
    val start = sorted.first().timestamp.truncatedTo(ChronoUnit.HOURS)
    val end = sorted.last().timestamp.truncatedTo(ChronoUnit.HOURS)
    val type = sorted.first().type

    val result = mutableListOf<HourlyHistoryPoint>()
    var current = start

    var safetyCounter = 0
    while (!current.isAfter(end) && safetyCounter < 1000) {
        val existing = sorted.find {
            it.timestamp.truncatedTo(ChronoUnit.HOURS).isEqual(current)
        }

        if (existing != null) {
            result.add(existing)
        } else {
            result.add(HourlyHistoryPoint(current, 0.0, type))
        }

        current = current.plusHours(1)
        safetyCounter++
    }

    return result
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun HourlyHistoryPreview() {
    HourlyHistory()
}