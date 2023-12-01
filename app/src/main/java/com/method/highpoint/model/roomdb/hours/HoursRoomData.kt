package com.method.highpoint.model.roomdb.hours

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hours_data")
data class HoursRoomData(
    @ColumnInfo(name = "rowId") var rowId: Int,
    @PrimaryKey @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "hours") var hours: String,
)