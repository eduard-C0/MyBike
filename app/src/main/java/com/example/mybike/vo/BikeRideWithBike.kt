package com.example.mybike.vo

import com.example.mybike.localdatasource.roomdb.bike.BikeEntity

data class BikeRideWithBike (
    val rideId: Long,
    val associatedBikeId: Long,
    val rideTitle: String?,
    val distanceInKm: Int,
    val durationInMinutes: Int,
    val date: String,
    val bike: BikeEntity
)