package com.vitiligo.breathe.presentation.location_details

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.breathe.data.remote.model.report.ReportType
import com.vitiligo.breathe.data.remote.model.report.PollutionReport
import com.vitiligo.breathe.presentation.shared.DetailBox
import com.vitiligo.breathe.presentation.shared.getReportColor
import com.vitiligo.breathe.presentation.shared.getReportIcon

@Composable
fun CommunityReports(
    modifier: Modifier = Modifier,
    reports: List<PollutionReport>
) {
    if (reports.isNotEmpty()) {
        DetailBox(
            label = "Community Reports",
            modifier = modifier
        ) {
            CommunityReportsContent(
                reports = reports
            )
        }
    }
}

@Composable
private fun CommunityReportsContent(
    modifier: Modifier = Modifier,
    reports: List<PollutionReport>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        reports.take(5).forEachIndexed { index, report ->
            ReportItem(report = report)

            if (index < reports.take(5).lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
        }
    }
}

@Composable
fun ReportItem(
    report: PollutionReport,
    modifier: Modifier = Modifier
) {
    val typeColor = getReportColor(report.reportType)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(typeColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getReportIcon(report.reportType),
                contentDescription = null,
                tint = typeColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.reportType.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = getRelativeTime(report.reportedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Reported by ${report.userName}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

private fun getRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()

    val timeMillis = if (timestamp < 10000000000L) timestamp * 1000 else timestamp

    return DateUtils.getRelativeTimeSpanString(
        timeMillis,
        now,
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}

@Preview(showBackground = true)
@Composable
private fun CommunityReportsPreview() {
    val mockReports = listOf(
        PollutionReport(
            id = "1",
            latitude = 0.0, longitude = 0.0,
            reportType = ReportType.BURNING,
            description = "Someone is burning plastic trash in the empty lot behind the market. Heavy black smoke.",
            reportedAt = System.currentTimeMillis() - 3600000,
            userName = "Ali K."
        ),
        PollutionReport(
            id = "2",
            latitude = 0.0, longitude = 0.0,
            reportType = ReportType.VEHICLE,
            description = "Old truck emitting excessive black exhaust stalled on the main road.",
            reportedAt = System.currentTimeMillis() - 7200000,
            userName = "Sarah"
        )
    )

    Column(Modifier.background(MaterialTheme.colorScheme.surface).padding(16.dp)) {
        CommunityReportsContent(reports = mockReports)
    }
}