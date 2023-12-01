package com.method.highpoint.model.roomdb.style

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "style_data")
data class StyleRoomData(
    @PrimaryKey @ColumnInfo(name = "styleId")
    val styleId: Int,
    @ColumnInfo(name = "styleName")
    val styleName: String?
)
