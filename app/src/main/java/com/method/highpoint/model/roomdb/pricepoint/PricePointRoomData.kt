package com.method.highpoint.model.roomdb.pricepoint

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "price_point_data")
data class PricePointRoomData(
    @PrimaryKey @ColumnInfo(name = "pricePointId")
    val pricePointId: Int?,
    @ColumnInfo(name = "pricePointName")
    val pricePointName: String?
)
