package com.vitiligo.breathe.domain.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.vitiligo.breathe.R
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.ui.theme.AQI_GREEN
import com.vitiligo.breathe.ui.theme.AQI_MAROON
import com.vitiligo.breathe.ui.theme.AQI_ORANGE
import com.vitiligo.breathe.ui.theme.AQI_PURPLE
import com.vitiligo.breathe.ui.theme.AQI_RED
import com.vitiligo.breathe.ui.theme.AQI_YELLOW

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

fun getAqiCategoryColor(category: AqiCategory): Color {
    return when (category) {
        AqiCategory.GREEN -> AQI_GREEN

        AqiCategory.YELLOW -> AQI_YELLOW

        AqiCategory.ORANGE -> AQI_ORANGE

        AqiCategory.RED -> AQI_RED

        AqiCategory.PURPLE -> AQI_PURPLE

        AqiCategory.MAROON -> AQI_MAROON
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
    if (category == AqiCategory.MAROON || category == AqiCategory.PURPLE) {
        return Color.White
    }

    return Color.Gray
}