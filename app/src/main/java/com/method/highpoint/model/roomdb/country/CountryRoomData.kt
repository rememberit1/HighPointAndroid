package com.method.highpoint.model.roomdb.country

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_data")
data class CountryRoomData(
    @PrimaryKey @ColumnInfo("exhibitorCountry")
    var exhibitorCountry: String
)
