package com.method.highpoint.model.shuttle

data class ShuttlesModel(
    val stopId: Int?,
    val stopNumber: Int?,
    val stopDescription: String?,
    val longitude: Double,
    val latitude: Double,
    val line: Int?
)
