package com.example.mybike.localdatasource.roomdb.ride

import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import kotlinx.coroutines.flow.Flow

interface IRideRepository {
    suspend fun addRide(rideEntity: RideEntity): Long

    suspend fun updateRide(rideEntity: RideEntity): Int

    suspend fun deleteRide(rideEntity: RideEntity): Int

    fun getAllRides(): Flow<List<RideEntity>>

    suspend fun getRide(rideId: Long): RideEntity
}