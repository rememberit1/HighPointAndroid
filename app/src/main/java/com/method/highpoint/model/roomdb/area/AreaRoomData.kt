package com.method.highpoint.model.roomdb.area

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_data")
data class AreaRoomData(
    @PrimaryKey @ColumnInfo("exhibitorAreaInterest")
    var exhibitorAreaInterest: String
)
