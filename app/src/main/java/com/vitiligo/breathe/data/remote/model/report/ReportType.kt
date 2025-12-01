package com.vitiligo.breathe.data.remote.model.report
import com.google.gson.annotations.SerializedName

enum class ReportType(val displayName: String) {
    @SerializedName("BURNING") BURNING("Garbage Burning"),
    @SerializedName("INDUSTRIAL") INDUSTRIAL("Industrial Smoke"),
    @SerializedName("VEHICLE") VEHICLE("Excessive Vehicle Smoke"),
    @SerializedName("CONSTRUCTION") CONSTRUCTION("Construction Dust"),
    @SerializedName("OTHER") OTHER("Other Hazard")
}
