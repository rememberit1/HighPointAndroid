package com.method.highpoint.model.event

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    val eventBuilding: String?,
    val eventEndDateTime: String?,
    val eventId: Int?,
    val eventLocation: String?,
    val eventStartDateTime: String?,
    val eventSummary: String?,
    val eventTitle: String?,
    val eventType: String?,
): Parcelable
