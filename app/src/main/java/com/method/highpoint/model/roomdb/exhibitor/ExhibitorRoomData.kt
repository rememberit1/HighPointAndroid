package com.method.highpoint.model.roomdb.exhibitor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exhibitor_data")
data class ExhibitorRoomData(
    @ColumnInfo(name = "rowId") var rowId: Int,
    @PrimaryKey @ColumnInfo(name = "exhibitorName")
    var exhibitorName: String,
    @ColumnInfo(name = "buildingAddress") var buildingAddress: String?,
    @ColumnInfo(name = "buildingFloor") var buildingFloor: String?,
    @ColumnInfo(name = "buildingFloorSort") var buildingFloorSort: Int?,
    @ColumnInfo(name = "buildingId") var buildingId: Int?,
    @ColumnInfo(name = "buildingMultiTenant") var buildingMultiTenant: Boolean?,
    @ColumnInfo(name = "buildingName") var buildingName: String?,
    @ColumnInfo(name = "buildingResourceCode") var buildingResourceCode: String?,
    @ColumnInfo(name = "buildingWing") var buildingWing: String?,
    @ColumnInfo(name = "exhibitorAreaInterest") var exhibitorAreaInterest: String?,
    @ColumnInfo(name = "exhibitorBustop") var exhibitorBustop: Int?,
    @ColumnInfo(name = "exhibitorBustopType") var exhibitorBustopType: String?,
    @ColumnInfo(name = "exhibitorNeighborhood") var exhibitorNeighborhood: String?,
    @ColumnInfo(name = "exhibitorCountry") var exhibitorCountry: String?,
    @ColumnInfo(name = "exhibitorDescription") var exhibitorDescription: String?,
    @ColumnInfo(name = "exhibitorFacebook") var exhibitorFacebook: String?,
    @ColumnInfo(name = "exhibitorId") var exhibitorId: Int?,
    @ColumnInfo(name = "exhibitorInstagram") var exhibitorInstagram: String?,
    @ColumnInfo(name = "exhibitorLinkedin") var exhibitorLinkedin: String?,
    @ColumnInfo(name = "exhibitorPinterest") var exhibitorPinterest: String?,
    @ColumnInfo(name = "exhibitorShowroom") var exhibitorShowroom: String?,
    @ColumnInfo(name = "exhibitorShowroomPhone") var exhibitorShowroomPhone: String?,
    @ColumnInfo(name = "exhibitorStatus") var exhibitorStatus: Boolean?,
    @ColumnInfo(name = "exhibitorTwitter") var exhibitorTwitter: String?,
    @ColumnInfo(name = "exhibitorWebsite") var exhibitorWebsite: String?,
    @ColumnInfo(name = "exhibitorYoutube") var exhibitorYoutube: String?,
    @ColumnInfo(name = "filter") var filter: List<Int>? = null
)