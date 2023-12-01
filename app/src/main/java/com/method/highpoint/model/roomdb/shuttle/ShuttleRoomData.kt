package com.method.highpoint.model.roomdb.shuttle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("shuttle_stops")
data class ShuttleRoomData(
    @PrimaryKey @ColumnInfo(name = "stopId")
    val stopId: Int?,
    @ColumnInfo(name = "stopNumber")
    val stopNumber: Int?,
    @ColumnInfo(name = "stopDescription")
    val stopDescription: String?,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "line")
    val line: Int?
)
