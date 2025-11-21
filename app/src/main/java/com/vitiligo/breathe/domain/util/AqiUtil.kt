package com.vitiligo.breathe.domain.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vitiligo.breathe.R
import com.vitiligo.breathe.domain.model.AqiCategory

fun getAqiCategoryLabel(category: AqiCategory): String {
    return when (category) {
        AqiCategory.GREEN -> "Good"

        AqiCategory.YELLOW -> "Moderate"

        AqiCategory.ORANGE -> "Unhealthy for sensitive groups"

        AqiCategory.RED -> "Unhealthy"

        AqiCategory.PURPLE -> "Very unhealthy"

        AqiCategory.MAROON -> "Hazardous"
    }
}

@Composable
fun getAqiCategoryColor(category: AqiCategory): Color {
    return when (category) {
        AqiCategory.GREEN -> colorResource(R.color.aqi_green)

        AqiCategory.YELLOW -> colorResource(R.color.aqi_yellow)

        AqiCategory.ORANGE -> colorResource(R.color.aqi_orange)

        AqiCategory.RED -> colorResource(R.color.aqi_red)

        AqiCategory.PURPLE -> colorResource(R.color.aqi_purple)

        AqiCategory.MAROON -> colorResource(R.color.aqi_maroon)
    }
}

@DrawableRes
fun getAqiFaceRes(category: AqiCategory): Int {
    return when (category) {
        AqiCategory.GREEN -> R.drawable.ic_face_2_green

        AqiCategory.YELLOW -> R.drawable.ic_face_3_yellow

        AqiCategory.ORANGE -> R.drawable.ic_face_4_orange

        AqiCategory.RED -> R.drawable.ic_face_5_red

        AqiCategory.PURPLE -> R.drawable.ic_face_6_purple

        AqiCategory.MAROON -> R.drawable.ic_face_7_maroon
    }
}

fun getOnAqiCategoryColor(category: AqiCategory): Color {
    if (category == AqiCategory.MAROON || category == AqiCategory.PURPLE || category == AqiCategory.RED) {
        return Color.White
    }

    return Color.Black
}