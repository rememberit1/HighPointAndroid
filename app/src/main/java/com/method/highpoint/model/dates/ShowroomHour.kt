package com.method.highpoint.model.dates

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowroomHour(
    val description: String,
    val hours: String,
    val id: Int
) : Parcelable