package com.example.mybike.vo

sealed class DisplayRideItem {
    data class Divider(val text: String) : DisplayRideItem()

    data class RideItemData(
        val rideId: Long,
        val associatedBikeId: Long,
        val rideTitle: String?,
        val distanceInKm: Int,
        val durationInMinutes: Int,
        val date: String,
        val bikeName: String
    ) : DisplayRideItem()
}
