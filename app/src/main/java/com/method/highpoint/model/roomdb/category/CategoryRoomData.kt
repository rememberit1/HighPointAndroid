package com.method.highpoint.model.roomdb.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_data")
data class CategoryRoomData(
    @PrimaryKey @ColumnInfo(name = "categoryId")
    var categoryId: Int?,
    @ColumnInfo(name = "parentId")
    var parentId: Int?,
    @ColumnInfo(name = "categoryParentName")
    var categoryParentName: String?,
    @ColumnInfo(name = "categorySubName")
    var categorySubName: String?
)
