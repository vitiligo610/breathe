package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.breathe.data.placeholder.placeholderLocationCardData
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ForecastDay
import com.vitiligo.breathe.domain.model.LocationCardData
import com.vitiligo.breathe.domain.util.getAqiCategoryColor
import com.vitiligo.breathe.domain.util.getAqiCategoryLabel
import com.vitiligo.breathe.domain.util.getAqiFaceRes
import com.vitiligo.breathe.domain.util.getOnAqiCategoryColor
import com.vitiligo.breathe.presentation.aqi.AqiValueBadge
import com.vitiligo.breathe.ui.theme.BreatheTheme

@Composable
fun LocationCard(
    data: LocationCardData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            val aqiColor = getAqiCategoryColor(data.currentAqiCategory)
            val onAqiColor = getOnAqiCategoryColor(data.currentAqiCategory)

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f)
                    .background(aqiColor)
                    .padding(top = 8.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = data.currentWeatherIconRes),
                        contentDescription = "Weather Icon",
                        tint = onAqiColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "${data.currentTempC}°",
                        color = onAqiColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = getAqiCategoryLabel(data.currentAqiCategory),
                    color = onAqiColor,
                    style = if (data.currentAqiCategory != AqiCategory.ORANGE) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Image(
                    painter = painterResource(id = getAqiFaceRes(data.currentAqiCategory)),
                    contentDescription = "AQI Face",
                    modifier = Modifier.size(58.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = data.currentAqi.toString(),
                        color = onAqiColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "US AQI+",
                        color = onAqiColor.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .weight(0.6f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = data.city,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = data.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.Start
                ) {
                    data.forecasts.take(3).forEachIndexed { index, day ->
                        ForecastColumn(
                            data = day,
                            modifier = Modifier.weight(1f)
                        )
                        if (index < 2 && index < data.forecasts.take(3).size - 1) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp)
                                    .padding(vertical = 12.dp)
                                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = "22:00",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastColumn(
    data: ForecastDay,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .width(IntrinsicSize.Min)
    ) {
        Text(
            text = data.dayLabel,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AqiValueBadge(
            value = data.aqi,
            category = data.aqiCategory
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(id = data.weatherIconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = "${data.highTempC}°",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "${data.lowTempC}°",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, device = PIXEL_9_PRO_XL)
@Composable
fun LocationCardPreview() {
    BreatheTheme {
        LocationCard(data = placeholderLocationCardData.get(0))
    }
}