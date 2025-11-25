package com.vitiligo.breathe.presentation.shared

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.breathe.R
import com.vitiligo.breathe.data.placeholder.mockAqiCardData
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.AqiCardData
import com.vitiligo.breathe.domain.util.getAqiCategoryColor
import com.vitiligo.breathe.domain.util.getAqiCategoryDarkColor
import com.vitiligo.breathe.domain.util.getAqiCategoryLabel
import com.vitiligo.breathe.domain.util.getAqiFaceRes
import com.vitiligo.breathe.domain.util.getOnAqiCategoryColor
import com.vitiligo.breathe.domain.util.getWeatherIconRes

@Composable
fun AqiCard(
    modifier: Modifier = Modifier,
    data: AqiCardData = mockAqiCardData,
) {
    val aqiColor = getAqiCategoryColor(data.category)
    val aqiColorDark = getAqiCategoryDarkColor(data.category)
    val onAqiColor = getOnAqiCategoryColor(data.category)

    val infiniteTransition = rememberInfiniteTransition(label = "shadowPulse")
    val animatedElevation = infiniteTransition.animateFloat(
        initialValue = 16f,
        targetValue = 32f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Reverse
        ), label = "shadowPulseValue"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = animatedElevation.value.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = aqiColor,
                spotColor = aqiColor
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(aqiColor)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(aqiColorDark),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = data.aqi.toString(),
                            color = onAqiColor,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "US AQI+",
                            color = onAqiColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                        )
                    }
                }

                Text(
                    text = getAqiCategoryLabel(data.category),
                    color = onAqiColor,
                    style = if (data.category != AqiCategory.ORANGE) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge.copy(fontSize = 21.sp),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                )

                Image(
                    painter = painterResource(id = getAqiFaceRes(data.category)),
                    contentDescription = "AQI Status Face",
                    modifier = Modifier.size(56.dp)
                )
            }

            HorizontalDivider(color = aqiColorDark, thickness = (0.5).dp, modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Main pollutant: ",
                    color = onAqiColor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = data.mainPollutant,
                    color = onAqiColor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${data.mainPollutantConcentration} ${data.mainPollutantConcentrationUnit}",
                    color = onAqiColor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Right,
                    modifier= Modifier
                        .weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeatherDetailItem(
                iconRes = getWeatherIconRes(data.weatherCode),
                value = "${data.tempC.toInt()}°",
                imageSize = 28.dp
            )

            WindDetailItem(
                windSpeed = data.windSpeedKph,
                windDeg = data.windSpeedDeg,
            )

            WeatherDetailItem(
                iconRes = R.drawable.ic_humidity_filled_24,
                value = "${data.humidityPercent.toInt()}%",
            )
        }
    }
}

@Composable
private fun WeatherDetailItem(
    iconRes: Int,
    value: String,
    modifier: Modifier = Modifier,
    imageSize: Dp = 20.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(imageSize)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun WindDetailItem(
    windSpeed: Double,
    windDeg: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_wind_direction_solid_24),
            contentDescription = "Wind Direction Arrow",
            modifier = Modifier
                .size(20.dp)
                .rotate(windDeg.toFloat())
        )
        Text(
            text = "$windSpeed mph",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AqiCardPreview() {
    MaterialTheme {
        AqiCard(
            modifier = Modifier.padding(16.dp)
        )
    }
}