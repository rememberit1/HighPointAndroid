package com.method.highpoint.model.dates

import com.google.gson.annotations.SerializedName

data class DatesModel(
    val marketDates: List<MarketDate>,
    val showroomHours: List<ShowroomHour>
)