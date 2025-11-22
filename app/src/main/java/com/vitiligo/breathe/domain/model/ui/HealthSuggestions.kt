package com.vitiligo.breathe.domain.model.ui

import androidx.annotation.DrawableRes
import com.vitiligo.breathe.domain.model.AqiCategory

data class HealthRec(
    val category: AqiCategory,
    val outdoorRec: String = "",
    @param:DrawableRes val outdoorRes: Int = 0,
    val windowsRec: String = "",
    @param:DrawableRes val windowsRes: Int = 0,
    val maskRec: String = "",
    @param:DrawableRes val maskRes: Int = 0,
    val purifierRec: String = "",
    @param:DrawableRes val purifierRes: Int = 0
)
