package com.example.mybike.localdatasource.roomdb.ride

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class RideEntity(
    @PrimaryKey(autoGenerate = true) val rideId: Long = 0,
    val associatedBikeId: Long,
    val rideTitle: String?,
    val distanceInKm: Int,
    val durationInMinutes: Int,
    val date: String
)
