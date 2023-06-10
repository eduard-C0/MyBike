package com.example.mybike.localdatasource.roomdb.bike

import kotlinx.coroutines.flow.Flow

interface IBikeRepository {

    suspend fun addBike(bikeEntity: BikeEntity): Long

    suspend fun updateBike(bikeEntity: BikeEntity): Int

    suspend fun deleteBike(bikeEntity: BikeEntity): Int

    fun getAllBikes(): Flow<List<BikeEntity>>

    suspend fun getBike(bikeId: Long): BikeEntity
}