package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    thickness: Dp = (0.8).dp,
    dashLength: Dp = 3.dp,
    gapLength: Dp = 2.dp
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .drawBehind {
                val dash = dashLength.toPx()
                val gap = gapLength.toPx()

                val pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(dash, gap),
                    phase = 0f
                )

                drawLine(
                    color = color,
                    start = Offset(x = center.x, y = 0f),
                    end = Offset(x = center.x, y = size.height),
                    strokeWidth = thickness.toPx(),
                    pathEffect = pathEffect
                )
            }
    )
}
