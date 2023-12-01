package com.method.highpoint.model.roomdb.building

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building_data")
data class BuildingRoomData(
    @PrimaryKey @ColumnInfo(name = "buildingId")
    var buildingId: Int?,
    @ColumnInfo(name = "buildingName")
    var buildingName: String?
)