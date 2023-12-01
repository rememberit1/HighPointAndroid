package com.method.highpoint.model.mymarket

data class MyMarketStar(
    val userGuid: String,
    val itemTypeId: Int,
    val itemType: String,
    val itemNotes: String? = "",
    val itemSchedule: String? = null,
    val itemLove: Boolean
)
