package com.vitiligo.breathe.domain.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.vitiligo.breathe.domain.model.ui.HistoryPoint
import java.time.temporal.ChronoUnit

enum class HistoryViewMode {
    Hourly,
    Daily
}

@RequiresApi(Build.VERSION_CODES.O)
fun normalizeData(
    data: List<HistoryPoint>,
    viewMode: HistoryViewMode
): List<HistoryPoint> {
    if (data.isEmpty()) return emptyList()

    val sorted = data.sortedBy { it.timestamp }
    val unit = if (viewMode == HistoryViewMode.Daily) ChronoUnit.DAYS else ChronoUnit.HOURS

    val start = sorted.first().timestamp.truncatedTo(unit)
    val end = sorted.last().timestamp.truncatedTo(unit)
    val type = sorted.first().type

    val result = mutableListOf<HistoryPoint>()
    var current = start

    var safetyCounter = 0
    while (!current.isAfter(end) && safetyCounter < 1000) {
        val existing = sorted.find {
            it.timestamp.truncatedTo(unit).isEqual(current)
        }

        if (existing != null) {
            result.add(existing)
        } else {
            result.add(HistoryPoint(current, 0.0, type))
        }

        current = if (viewMode == HistoryViewMode.Daily) current.plusDays(1) else current.plusHours(1)
        safetyCounter++
    }

    return result
}
