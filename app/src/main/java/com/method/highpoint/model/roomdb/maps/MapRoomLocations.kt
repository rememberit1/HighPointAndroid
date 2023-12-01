package com.method.highpoint.model.roomdb.maps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.method.highpoint.model.maps.BuildingExhibitorsItem

@Entity(tableName = "map_locations")
data class MapRoomLocations(
    @PrimaryKey @ColumnInfo(name = "rowId") var rowId: Int,
    @ColumnInfo("buildingId")
    val buildingId: Int?,
    @ColumnInfo(name = "buildingName")
    val buildingName: String,
    @ColumnInfo("exhibitorId")
    val exhibitorId: Int?,
    @ColumnInfo("latitude")
    val latitude: Double,
    @ColumnInfo("longitude")
    val longitude: Double,
    @ColumnInfo("tenantCount")
    val tenantCount: Int?,
    @ColumnInfo("buildingExhibitors")
    val buildingExhibitors: List<BuildingExhibitorsItem>?
)
