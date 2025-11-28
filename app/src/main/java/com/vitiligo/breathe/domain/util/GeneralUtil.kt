package com.vitiligo.breathe.domain.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.vitiligo.breathe.domain.model.Coordinates

fun Color.darken(factor: Float = 0.8f): Color {
    val argb = this.toArgb()

    val hsl = floatArrayOf(0f, 0f, 0f)
    ColorUtils.colorToHSL(argb, hsl)

    hsl[2] *= factor

    hsl[2] = maxOf(0f, hsl[2])

    return Color(ColorUtils.HSLToColor(hsl))
}

fun Modifier.shadowWithClipIntersect(
    elevation: Dp,
    shape: Shape = RectangleShape,
    clip: Boolean = elevation > 0.dp,
    ambientColor: Color = DefaultShadowColor,
    spotColor: Color = DefaultShadowColor,
): Modifier = this
    .drawWithCache {
        //  bottom shadow offset in Px based on elevation
        val bottomOffsetPx = elevation.toPx() * 2.2f
        // Adjust the size to extend the bottom by the bottom shadow offset
        val adjustedSize = Size(size.width, size.height + bottomOffsetPx)
        val outline = shape.createOutline(adjustedSize, layoutDirection, this)
        val path = Path().apply { addOutline(outline) }
        onDrawWithContent {
            clipPath(path, ClipOp.Intersect) {
                this@onDrawWithContent.drawContent()
            }
        }
    }
    .shadow(elevation, shape, clip, ambientColor, spotColor)

fun String.toCoordinates(): Coordinates {
    val values = this.split(",")

    if (values.size != 2) {
        throw IllegalArgumentException(
            "Invalid coordinates format: Expected 'lat,lon', but found ${values.size} components in '$this'"
        )
    }

    val latString = values[0].trim()
    val lonString = values[1].trim()

    val latitude = latString.toDoubleOrNull()
        ?: throw IllegalArgumentException("Invalid latitude value: '$latString' is not a valid number.")

    val longitude = lonString.toDoubleOrNull()
        ?: throw IllegalArgumentException("Invalid longitude value: '$lonString' is not a valid number.")

    return Coordinates(
        latitude = latitude,
        longitude = longitude
    )
}