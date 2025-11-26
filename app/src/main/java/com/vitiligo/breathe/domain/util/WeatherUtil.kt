package com.vitiligo.breathe.domain.util;

import androidx.annotation.DrawableRes
import com.vitiligo.breathe.R

@DrawableRes
fun getWeatherIconRes(weatherCode: Int, utcOffsetSeconds: Int = 0): Int {
    return when (weatherCode) {
        // Clear Sky (WMO 0)
        0 -> R.drawable.ic_weather_clear_sky_full_32

        // Few Clouds (WMO 1, 2)
        1, 2 -> R.drawable.ic_weather_few_clouds_full_32

        // Scattered Clouds (WMO 3)
        3 -> R.drawable.ic_weather_scattered_clouds_full_32

        // Cloudy (WMO 4, 5)
        4, 5 -> R.drawable.ic_weather_cloudy_full_32

        // Rain/Showers (WMO 8, 9, 21, 22, 23, 24, 25, 26, 27)
        8, 9, 21, 22, 23, 24, 25, 26, 27 -> R.drawable.ic_weather_rain_full_32

        // Drizzle (WMO 51, 53, 55, 56, 57)
        51, 53, 55, 56, 57 -> R.drawable.ic_weather_scattered_showers_full_32

        // Rain/Showers (WMO 61, 63, 65, 66, 67, 80, 81, 82)
        61, 63, 65, 66, 67, 80, 81, 82 -> R.drawable.ic_weather_rain_full_32

        // Snow (WMO 71, 73, 75, 77, 83, 84, 85, 86)
        71, 73, 75, 77, 83, 84, 85, 86 -> R.drawable.ic_weather_snow_full_32

        // Mist/Fog (WMO 6, 7, 10, 11, 12, 40-49)
        in 40..49, 6, 7, 10, 11, 12 -> R.drawable.ic_weather_mist_full_32

        // Thunderstorms (WMO 17, 29, 90-99)
        17, 29, in 90..99 -> R.drawable.ic_weather_thunderstorms_full_32

        // Default (or other less common codes)
        else -> R.drawable.ic_weather_cloudy_full_32
    }
}
