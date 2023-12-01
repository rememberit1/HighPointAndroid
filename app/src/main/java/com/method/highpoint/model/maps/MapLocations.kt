package com.method.highpoint.model.maps

data class MapLocations(
    val buildingId: Int?,
    val buildingName: String,
    val exhibitorId: Int?,
    val latitude: Double,
    val longitude: Double,
    val tenantCount: Int?,
    val buildingExhibitors: List<BuildingExhibitorsItem>?
)
