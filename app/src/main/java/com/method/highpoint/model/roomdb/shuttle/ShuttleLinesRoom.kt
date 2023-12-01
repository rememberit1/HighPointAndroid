package com.method.highpoint.model.roomdb.shuttle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.method.highpoint.model.shuttle.ShuttleLineLocationsItem

@Entity(tableName = "shuttle_lines")
data class ShuttleLinesRoom(
    @PrimaryKey @ColumnInfo(name = "rowId") var rowId: Int,
    @ColumnInfo(name = "shuttleLine")
    val shuttleLine: Int?,
    @ColumnInfo(name = "stopNumberStart")
    val stopNumberStart: Int?,
    @ColumnInfo(name = "stopNumberEnd")
    val stopNumberEnd: Int?,
    @ColumnInfo(name = "shuttleLineLocations")
    val shuttleLineLocations: List<ShuttleLineLocationsItem>
)
