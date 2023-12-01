package com.method.highpoint.model.roomdb.options

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "option_data")
data class OptionRoomData(
    @PrimaryKey @ColumnInfo(name = "optionId")
    val optionId: Int,
    @ColumnInfo(name = "optionName")
    val optionName: String?
)