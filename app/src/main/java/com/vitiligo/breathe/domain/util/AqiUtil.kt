package com.vitiligo.breathe.domain.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vitiligo.breathe.R
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.Pollutant
import com.vitiligo.breathe.domain.model.ui.HealthRec

fun getAqiCategory(value: Int): AqiCategory {
    return when {
        value <= 50 -> AqiCategory.GREEN
        value <= 100 -> AqiCategory.YELLOW
        value <= 150 -> AqiCategory.ORANGE
        value <= 200 -> AqiCategory.RED
        value <= 300 -> AqiCategory.PURPLE
        else -> AqiCategory.MAROON
    }
}

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

fun getAQICategoryColor(category: AqiCategory): Color {
    return when (category) {
        AqiCategory.GREEN -> Color(0xFFABD162)

        AqiCategory.YELLOW -> Color(0xFFF7D45F)

        AqiCategory.ORANGE -> Color(0xFFFC9957)

        AqiCategory.RED -> Color(0xFFF7666C)

        AqiCategory.PURPLE -> Color(0xFFA37DB8)

        AqiCategory.MAROON -> Color(0xFFA17584)
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

fun getPollutantCategory(reading: Double, pollutantKey: String): AqiCategory {
    val normalizedKey = pollutantKey.lowercase()

    // Molecular weights for conversion (g/mol)
    val O3_MW = 48.0
    val NO2_MW = 46.0
    val SO2_MW = 64.1
    val CO_MW = 28.0

    // Standard molar volume at 25°C, 1 atm is 24.45 L/mol.
    // Conversion factor (ug/m³ to ppb) = (24.45 / MW)
    // Conversion factor (ug/m³ to ppm) = (24.45 / (MW * 1000))

    return when (normalizedKey) {
        // PM2.5 (Fine Particulate Matter) - 24h avg - Unit: µg/m³ (No conversion needed)
        "pm2_5" -> {
            val value = reading // Value already in µg/m³
            when {
                value <= 12.0 -> AqiCategory.GREEN
                value <= 35.5 -> AqiCategory.YELLOW
                value <= 55.5 -> AqiCategory.ORANGE
                value <= 150.5 -> AqiCategory.RED
                value <= 250.5 -> AqiCategory.PURPLE
                else -> AqiCategory.MAROON
            }
        }

        // PM10 (Coarse Particulate Matter) - 24h avg - Unit: µg/m³ (No conversion needed)
        "pm10" -> {
            val value = reading // Value already in µg/m³
            when {
                value <= 55.0 -> AqiCategory.GREEN
                value <= 155.0 -> AqiCategory.YELLOW
                value <= 255.0 -> AqiCategory.ORANGE
                value <= 355.0 -> AqiCategory.RED
                value <= 425.0 -> AqiCategory.PURPLE
                else -> AqiCategory.MAROON
            }
        }

        // O3 (Ozone) - 8h avg - Unit: ppb (Conversion: µg/m³ to ppb)
        // Conversion Factor: 24.45 / 48.0 ≈ 0.509
        "o3" -> {
            val value = reading * (24.45 / O3_MW)
            when {
                value <= 55.0 -> AqiCategory.GREEN
                value <= 70.0 -> AqiCategory.YELLOW
                value <= 85.0 -> AqiCategory.ORANGE
                value <= 105.0 -> AqiCategory.RED
                value <= 200.0 -> AqiCategory.PURPLE // Used 200 based on the table's next category start (205)
                else -> AqiCategory.MAROON
            }
        }

        // NO2 (Nitrogen Dioxide) - 1h avg - Unit: ppb (Conversion: µg/m³ to ppb)
        // Conversion Factor: 24.45 / 46.0 ≈ 0.531
        "no2" -> {
            val value = reading * (24.45 / NO2_MW)
            when {
                value <= 54.0 -> AqiCategory.GREEN
                value <= 100.0 -> AqiCategory.YELLOW
                value <= 360.0 -> AqiCategory.ORANGE
                value <= 650.0 -> AqiCategory.RED
                value <= 1250.0 -> AqiCategory.PURPLE
                else -> AqiCategory.MAROON
            }
        }

        // SO2 (Sulfur Dioxide) - 24h avg - Unit: ppb (Conversion: µg/m³ to ppb)
        // Note: Using 24h avg breakpoints as 1h avg range in table is incomplete.
        // Conversion Factor: 24.45 / 64.1 ≈ 0.381
        "so2" -> {
            val value = reading * (24.45 / SO2_MW)
            when {
                // The table only lists thresholds for Very Unhealthy (305 ppb) and Hazardous (605 ppb) for 24h.
                // We'll use the most common GREEN/YELLOW/ORANGE thresholds (approx. 35/75/185 ppb) for completeness
                // but strictly apply the table's high-end values.
                value <= 35.0 -> AqiCategory.GREEN
                value <= 75.0 -> AqiCategory.YELLOW
                value <= 185.0 -> AqiCategory.ORANGE
                value <= 305.0 -> AqiCategory.RED
                value <= 605.0 -> AqiCategory.PURPLE
                else -> AqiCategory.MAROON
            }
        }

        // CO (Carbon Monoxide) - 8h avg - Unit: ppm (Conversion: µg/m³ to ppm)
        // Conversion Factor: 24.45 / (28.0 * 1000) ≈ 0.000873
        "co" -> {
            val value = reading * (24.45 / (CO_MW * 1000.0))
            when {
                value <= 4.5 -> AqiCategory.GREEN
                value <= 9.5 -> AqiCategory.YELLOW
                value <= 12.5 -> AqiCategory.ORANGE
                value <= 15.5 -> AqiCategory.RED
                value <= 30.5 -> AqiCategory.PURPLE
                else -> AqiCategory.MAROON
            }
        }

        else -> getPollutantCategory(reading)
    }
}

fun getPollutantDetails(key: String, reading: Double, unit: String = "µg/m³"): Pollutant {
    return Pollutant(
        key = key,
        displayValue = getPollutantDisplayValue(key),
        description = getPollutantDescription(key),
        reading = reading,
        unit = unit,
        category = getPollutantCategory(reading, key)
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
