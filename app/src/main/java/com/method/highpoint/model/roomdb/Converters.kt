package com.method.highpoint.model.roomdb


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.method.highpoint.model.maps.BuildingExhibitorsItem
import com.method.highpoint.model.shuttle.ShuttleLineLocationsItem

class Converters {

    @TypeConverter
    fun fromList(value: List<Int>?): String = value.toString()

    @TypeConverter
    fun toList(value: String): List<Int> {
        val result = ArrayList<Int>()
        val split =value.replace("[","").replace("]","").replace(" ","").split(",")
        for (n in split) {
            try {
                result.add(n.toInt())
            } catch (_: Exception) {

            }
        }
        return result
    }

    @TypeConverter
    fun listToJson(value: List<ShuttleLineLocationsItem>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<ShuttleLineLocationsItem>::class.java).toList()

    @TypeConverter
    fun listItemToJson(value: List<BuildingExhibitorsItem>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToItemList(value: String) = Gson().fromJson(value, Array<BuildingExhibitorsItem>::class.java).toList()

}