package com.vitiligo.breathe.presentation.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Factory
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.R

@Composable
fun getReportIcon(type: ReportType): ImageVector {
    return when (type) {
        ReportType.BURNING -> Icons.Rounded.LocalFireDepartment
        ReportType.INDUSTRIAL -> Icons.Rounded.Factory
        ReportType.VEHICLE -> Icons.Rounded.DirectionsCar
        ReportType.CONSTRUCTION -> Icons.Rounded.Construction
        ReportType.OTHER -> Icons.Rounded.Warning
    }
}

fun getDrawableIdForReport(type: ReportType): Int {
    return when (type) {
        ReportType.BURNING -> R.drawable.baseline_local_fire_department_24
        ReportType.INDUSTRIAL -> R.drawable.baseline_factory_24
        ReportType.VEHICLE -> R.drawable.baseline_directions_car_24
        ReportType.CONSTRUCTION -> R.drawable.baseline_construction_24
        ReportType.OTHER -> R.drawable.baseline_warning_24
    }
}

fun getReportColor(type: ReportType): Color {
    return when (type) {
        ReportType.BURNING -> Color(0xFFFF5252) // Red-ish
        ReportType.INDUSTRIAL -> Color(0xFF607D8B) // Grey-Blue
        ReportType.VEHICLE -> Color(0xFF5C6BC0) // Indigo
        ReportType.CONSTRUCTION -> Color(0xFFFFA726) // Orange
        ReportType.OTHER -> Color(0xFFFF7043) // Deep Orange
    }
}