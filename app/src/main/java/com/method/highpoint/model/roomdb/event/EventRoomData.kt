package com.method.highpoint.model.roomdb.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_data")
data class EventRoomData(
    @ColumnInfo(name = "rowId") var rowId: Int,
    @PrimaryKey @ColumnInfo(name = "eventTitle")
    var eventTitle: String,
    @ColumnInfo(name = "eventBuilding") var eventBuilding: String?,
    @ColumnInfo(name = "eventSummary") var eventSummary: String?,
    @ColumnInfo(name = "eventId") var eventId: Int?,
    @ColumnInfo(name = "eventLocation") var eventLocation: String?,
    @ColumnInfo(name = "eventStartDateTime") var eventStartDateTime: String?,
    @ColumnInfo(name = "eventEndDateTime") var eventEndDateTime: String?,
    @ColumnInfo(name = "eventType") var eventType: String?,
)