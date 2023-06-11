package com.example.mybike.vo

data class BikeDataItem(
    val bikeId: Long,
    val bikeName: String,
    val bikeType: BikeType,
    val inchWheelSize: String,
    val bikeColor: Int,
    val distanceServiceDueInKm: Int,
    val traveledDistanceInKm: Int = 0
)
