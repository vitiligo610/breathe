package com.vitiligo.breathe.domain.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vitiligo.breathe.R
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.Pollutant
import com.vitiligo.breathe.domain.model.ui.HealthRec

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

@Composable
fun getAqiCategoryDarkColor(category: AqiCategory): Color {
    return when (category) {
        AqiCategory.GREEN -> colorResource(R.color.aqi_green_dark)

        AqiCategory.YELLOW -> colorResource(R.color.aqi_yellow_dark)

        AqiCategory.ORANGE -> colorResource(R.color.aqi_orange_dark)

        AqiCategory.RED -> colorResource(R.color.aqi_red_dark)

        AqiCategory.PURPLE -> colorResource(R.color.aqi_purple_dark)

        AqiCategory.MAROON -> colorResource(R.color.aqi_maroon_dark)
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

@Composable
fun getAqiScoreColor(score: Int): Color {
    return if (score <= 50) {
        colorResource(R.color.aqi_green)
    } else if (score <= 100) {
        colorResource(R.color.aqi_yellow)
    } else if (score <= 150) {
        colorResource(R.color.aqi_orange)
    } else if (score <= 200) {
        colorResource(R.color.aqi_red)
    } else if (score <= 300) {
        colorResource(R.color.aqi_purple)
    } else {
        colorResource(R.color.aqi_maroon)
    }
}

fun getPollutantDisplayValue(key: String): String {
    return when (key) {
        "pm2_5" -> "PM2.5"
        "pm10" -> "PM10"
        "o3" -> "O₃"
        "no2" -> "NO₂"
        "so2" -> "SO₂"
        "co" -> "CO"
        else -> key
    }
}

fun getPollutantDescription(key: String): String {
    return when (key) {
        "pm2_5" -> "Fine particles"
        "pm10" -> "Coarse particles"
        "o3" -> "Ozone"
        "no2" -> "Nitrogen Dioxide"
        "so2" -> "Sulphur Dioxide"
        "co" -> "Carbon Monoxide"
        else -> key
    }
}

fun getPollutantCategory(reading: Double): AqiCategory {
    return if (reading <= 50) {
        AqiCategory.GREEN
    } else if (reading <= 100) {
        AqiCategory.YELLOW
    } else if (reading <= 150) {
        AqiCategory.ORANGE
    } else if (reading <= 200) {
        AqiCategory.RED
    } else if (reading <= 300) {
        AqiCategory.PURPLE
    } else {
        AqiCategory.MAROON
    }
}

fun getPollutantDetails(key: String, reading: Double, unit: String = "µg/m³"): Pollutant {
    return Pollutant(
        key = key,
        displayValue = getPollutantDisplayValue(key),
        description = getPollutantDescription(key),
        reading = reading,
        unit = unit,
        category = getPollutantCategory(reading)
    )
}

@Composable
fun getHealthRecommendations(category: AqiCategory): HealthRec {
    val outdoorRec = "Avoid outdoor exercise"
    val windowsRec = "Close your windows to avoid dirty outdoor air"
    val maskRec = "Wear a mask outdoors"
    val purifierRec = "Run an air purifier"

    return when (category) {
        AqiCategory.GREEN -> HealthRec(
            category = category,
            outdoorRec = "Enjoy outdoor activities", outdoorRes = R.drawable.ic_exercise_green,
            windowsRec = "Open your windows to bring clean, fresh air indoors", windowsRes = R.drawable.ic_window_green
        )

        AqiCategory.YELLOW -> HealthRec(
            category = category,
            outdoorRec = "Sensitive groups should reduce outdoor exercise", outdoorRes = R.drawable.ic_exercise_yellow,
            windowsRec = "Close your windows to avoid dirty outdoor air", windowsRes = R.drawable.ic_window_yellow,
            maskRec = "Sensitive groups should wear a mask outdoors", maskRes = R.drawable.ic_mask_yellow,
            purifierRec = "Sensitive groups should run an air purifier",  purifierRes = R.drawable.ic_purifier_yellow
        )

        AqiCategory.ORANGE -> HealthRec(
            category = category,
            outdoorRec = "Reduce outdoor exercise", outdoorRes = R.drawable.ic_exercise_orange,
            windowsRec = "Close your windows to avoid dirty outdoor air", windowsRes = R.drawable.ic_window_orange,
            maskRec = "Sensitive groups should wear a mask outdoors", maskRes = R.drawable.ic_mask_orange,
            purifierRec = "Run an air purifier", purifierRes = R.drawable.ic_purifier_orange
        )

        AqiCategory.RED -> HealthRec(
            category = category,
            outdoorRec = outdoorRec,  outdoorRes = R.drawable.ic_exercise_red,
            windowsRec = windowsRec, windowsRes = R.drawable.ic_window_red,
            maskRec = maskRec, maskRes = R.drawable.ic_mask_red,
            purifierRec = purifierRec, purifierRes = R.drawable.ic_purifier_red
        )

        AqiCategory.PURPLE -> HealthRec(
            category = category,
            outdoorRec = outdoorRec,  outdoorRes = R.drawable.ic_exercise_purple,
            windowsRec = windowsRec, windowsRes = R.drawable.ic_window_purple,
            maskRec = maskRec, maskRes = R.drawable.ic_mask_purple,
            purifierRec = purifierRec, purifierRes = R.drawable.ic_purifier_purple
        )

        AqiCategory.MAROON -> HealthRec(
            category = category,
            outdoorRec = outdoorRec,  outdoorRes = R.drawable.ic_exercise_maroon,
            windowsRec = windowsRec, windowsRes = R.drawable.ic_window_maroon,
            maskRec = maskRec, maskRes = R.drawable.ic_mask_maroon,
            purifierRec = purifierRec, purifierRes = R.drawable.ic_purifier_maroon
        )
    }
}
