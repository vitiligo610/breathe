package com.vitiligo.breathe.domain.converter

import androidx.room.TypeConverter
import com.vitiligo.breathe.domain.model.AqiCategory

class AqiCategoryConverter {
    @TypeConverter
    fun fromAqiCategory(category: AqiCategory): String {
        return category.label
    }

    @TypeConverter
    fun toAqiCategory(label: String): AqiCategory {
        return AqiCategory.entries.first { it.label == label }
    }
}
