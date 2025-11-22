package com.vitiligo.breathe.presentation.location_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitiligo.breathe.R
import com.vitiligo.breathe.ui.theme.BreatheTheme

val greenSurface = Color(0xFFECFDF3)
val greenOutline = Color(0xFFD1FADF)

val redSurface = Color(0xFFFEF3F2)
val redOutline = Color(0xFFFEE4E2)
val redPrimary = Color(0xFFF04438)

@Composable
fun ReadingMetCard(
    modifier: Modifier = Modifier,
    pollutant: String = "PM2.5",
    threshold: Double = 10.0,
    reading: Double = 219.5,
) {
    val times = reading / threshold
    val thresholdExceeded = times > 1

    val surface = if (!thresholdExceeded) greenSurface else redSurface
    val outline = if (!thresholdExceeded) greenOutline else redOutline
    val icon = if (!thresholdExceeded) R.drawable.ic_round_check_green else R.drawable.ic_round_warning_red

    val shape = RoundedCornerShape(16.dp)

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .border(
                width = 1.dp,
                color = outline,
                shape = shape
            )
            .background(color = surface, shape)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
        )

        val specialStyle = SpanStyle(
            fontWeight = FontWeight.ExtraBold,
            color = redPrimary
        )

        val timesString = String.format("%.1f", times)

        val styledText = buildAnnotatedString {
            if (!thresholdExceeded) {
                append("$pollutant concentration currently meets the WHO annual air quality guideline value")
            } else {
                append("$pollutant concentration currently ")

                withStyle(style = specialStyle) {
                    append(timesString)
                }

                append(" times the WHO annual air quality guideline value")
            }
        }

        Text(
            text = styledText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 12.dp)
        )
    }
}

@Preview
@Composable
private fun ReadingMetCardPreview() {
    BreatheTheme {
        ReadingMetCard()
    }
}