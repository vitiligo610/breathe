package com.vitiligo.breathe.data.placeholder

import android.os.Build
import androidx.annotation.RequiresApi
import com.vitiligo.breathe.domain.model.ui.HistoryTabOption
import com.vitiligo.breathe.domain.model.ui.HourlyHistoryPoint
import java.time.LocalDateTime
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
val now: LocalDateTime = LocalDateTime.now().withMinute(0)
@RequiresApi(Build.VERSION_CODES.O)
val mockHourlyHistoryData = (0..23).map { i ->
    val time = now.minusHours(23 - i.toLong())
    val value = 100 + (50 * sin(i * 0.3)).toInt()
    HourlyHistoryPoint(
        timestamp = time,
        value = value.toDouble(),
        type = HistoryTabOption.AQI
    )
}
