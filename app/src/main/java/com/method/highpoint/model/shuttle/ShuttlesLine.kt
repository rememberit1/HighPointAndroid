package com.method.highpoint.model.shuttle

data class ShuttlesLine(
    val shuttleLine: Int?,
    val stopNumberStart: Int?,
    val stopNumberEnd: Int?,
    val shuttleLineLocations: List<ShuttleLineLocationsItem>
)
