package com.vitiligo.breathe.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.util.getAqiScoreColor
import com.vitiligo.breathe.domain.util.getAqiCategoryColor

@Composable
fun AqiCategoryIndicator(
    modifier: Modifier = Modifier,
    aqiScore: Int = 0,
    aqiCategory: AqiCategory? = null,
    radius: Dp = 4.dp
) {
    val color = if (aqiCategory != null) getAqiCategoryColor(aqiCategory) else getAqiScoreColor(aqiScore)

    Box(
        modifier = modifier
            .size(radius * 2)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}