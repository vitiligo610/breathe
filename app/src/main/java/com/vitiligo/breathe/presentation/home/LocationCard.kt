package com.vitiligo.breathe.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitiligo.breathe.data.placeholder.placeholderLocationCardData
import com.vitiligo.breathe.domain.model.AqiCategory
import com.vitiligo.breathe.domain.model.ui.ForecastDay
import com.vitiligo.breathe.domain.model.ui.LocationCardData
import com.vitiligo.breathe.domain.util.getAqiCategoryColor
import com.vitiligo.breathe.domain.util.getAqiCategoryLabel
import com.vitiligo.breathe.domain.util.getAqiFaceRes
import com.vitiligo.breathe.domain.util.getOnAqiCategoryColor
import com.vitiligo.breathe.domain.util.getWeatherIconRes
import com.vitiligo.breathe.presentation.shared.AqiValueBadge
import com.vitiligo.breathe.presentation.shared.DashedVerticalDivider
import com.vitiligo.breathe.ui.theme.BreatheTheme

@Composable
fun LocationCard(
    data: LocationCardData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
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
                    .weight(0.3f)
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
                        painter = painterResource(id = getWeatherIconRes(data.currentWeatherIcon)),
                        contentDescription = "Weather Icon",
                        tint = if (onAqiColor == Color.Black) onAqiColor.copy(alpha = 0.4f) else onAqiColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "${data.currentTempC}°",
                        color = onAqiColor,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                Text(
                    text = getAqiCategoryLabel(data.currentAqiCategory),
                    color = onAqiColor,
                    style = if (data.currentAqiCategory != AqiCategory.ORANGE) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .height(38.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )

                Image(
                    painter = painterResource(id = getAqiFaceRes(data.currentAqiCategory)),
                    contentDescription = "AQI Face",
                    modifier = Modifier
                        .size(54.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = data.currentAqi.toString(),
                        color = onAqiColor,
                        fontSize = 31.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    Text(
                        text = "US AQI+",
                        color = onAqiColor.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .offset(y = (-8).dp)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .weight(0.7f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = data.city,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = data.subtitle,
                            fontSize = 13.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    data.forecasts.take(3).forEachIndexed { index, day ->
                        ForecastColumn(
                            data = day,
                        )
                        
                        if (index < 2 && index < data.forecasts.take(3).size - 1) {
                            DashedVerticalDivider(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = data.time,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            modifier = Modifier
                                .size(18.dp)
                        )
                    }
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
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        AqiValueBadge(
            value = data.aqi,
            category = data.aqiCategory
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(top = 2.dp)
        ) {
            Image(
                painter = painterResource(id = getWeatherIconRes(data.weatherIcon)),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${data.highTempC}°",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 13.sp
                )
                Text(
                    text = "${data.lowTempC}°",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    lineHeight = 13.sp
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