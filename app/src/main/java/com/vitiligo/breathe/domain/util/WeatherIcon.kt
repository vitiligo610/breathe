package com.vitiligo.breathe.domain.util;

import androidx.annotation.DrawableRes
import com.vitiligo.breathe.R

@DrawableRes
fun getWeatherIconRes(iconCode: String): Int {
    return when (iconCode) {
        // Clear Sky
        "01d" -> R.drawable.ic_weather_clear_sky_full_32
        "01n" -> R.drawable.ic_weather_night_clear_sky_full_32

        // Few Clouds
        "02d" -> R.drawable.ic_weather_few_clouds_full_32
        "02n" -> R.drawable.ic_weather_night_few_clouds_full_32

        // Scattered Clouds (Day/Night share the same icon in your list)
        "03d", "03n" -> R.drawable.ic_weather_scattered_clouds_full_32

        // Broken Clouds (Mapped to 'cloudy')
        "04d", "04n" -> R.drawable.ic_weather_cloudy_full_32

        // Shower Rain (Mapped to 'scattered showers')
        "09d", "09n" -> R.drawable.ic_weather_scattered_showers_full_32

        // Rain
        "10d" -> R.drawable.ic_weather_rain_full_32
        "10n" -> R.drawable.ic_weather_night_rain_full_32

        // Thunderstorm
        "11d", "11n" -> R.drawable.ic_weather_thunderstorms_full_32

        // Snow
        "13d", "13n" -> R.drawable.ic_weather_snow_full_32

        // Mist
        "50d", "50n" -> R.drawable.ic_weather_mist_full_32

        else -> R.drawable.ic_weather_clear_sky_full_32
    }
}
