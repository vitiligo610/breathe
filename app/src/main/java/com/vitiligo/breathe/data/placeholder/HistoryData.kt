package com.vitiligo.breathe.data.placeholder

import android.os.Build
import androidx.annotation.RequiresApi
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import java.time.LocalDateTime
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
val now: LocalDateTime = LocalDateTime.now().withMinute(0)
@RequiresApi(Build.VERSION_CODES.O)
val mockHourlyHistoryData = (0..23).map { i ->
    val time = now.minusHours(23 - i.toLong())
    val value = 100 + (50 * sin(i * 0.3)).toInt()
    HistoryPoint(
        timestamp = time,
        value = value.toDouble(),
        type = HistoryTabOption.AQI
    )
}

@RequiresApi(Build.VERSION_CODES.O)
val mockDailyHistoryData = (0..29).map { i ->
    val time = now.minusDays(29 - i.toLong())
    val value = 50 + (20 * sin(i * 0.7)).toInt()
    val finalValue = maxOf(10, value)
    HistoryPoint(
        timestamp = time.withHour(0).withMinute(0).withSecond(0).withNano(0),
        value = finalValue.toDouble(),
        type = HistoryTabOption.AQI
    )
}
