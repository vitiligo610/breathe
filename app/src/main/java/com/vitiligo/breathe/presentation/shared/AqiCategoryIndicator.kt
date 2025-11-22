package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.domain.util.getAqiScoreColor

@Composable
fun AqiCategoryIndicator(
    aqiScore: Int,
    modifier: Modifier = Modifier,
    radius: Dp = 4.dp
) {
    val color = getAqiScoreColor(aqiScore)

    Box(
        modifier = modifier
            .size(radius * 2)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}