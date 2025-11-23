package com.vitiligo.breathe.domain.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun Color.darken(factor: Float = 0.8f): Color {
    val argb = this.toArgb()

    val hsl = floatArrayOf(0f, 0f, 0f)
    ColorUtils.colorToHSL(argb, hsl)

    hsl[2] *= factor

    hsl[2] = maxOf(0f, hsl[2])

    return Color(ColorUtils.HSLToColor(hsl))
}
