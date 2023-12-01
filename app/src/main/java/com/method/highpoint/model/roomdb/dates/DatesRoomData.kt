package com.method.highpoint.model.roomdb.dates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_data")
data class DatesRoomData(
    @ColumnInfo(name = "rowId") var rowId: Int,
    @PrimaryKey @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "active") var active: Boolean,
    @ColumnInfo(name = "closingDate") var closingDate: String,
    @ColumnInfo(name = "eventDate") var eventDate: String,
    @ColumnInfo(name = "marketName") var marketName: String,
    @ColumnInfo(name = "openingDate") var openingDate: String
)