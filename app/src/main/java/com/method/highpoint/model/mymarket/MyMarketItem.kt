package com.method.highpoint.model.mymarket

data class MyMarketItem(
    val itemId: Int,
    val itemTypeId : Int,
    val itemType: String? = "",
    val itemNotes: String? = "",
    val itemSchedule: String? = "",
    val itemLove: Boolean,
    val userGuid: String
)
