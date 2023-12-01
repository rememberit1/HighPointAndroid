package com.method.highpoint.model.dates

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarketDate(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("closingDate")
    val closingDate: String,
    @SerializedName("eventDate")
    val eventDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("marketName")
    val marketName: String,
    @SerializedName("openingDate")
    val openingDate: String
) : Parcelable