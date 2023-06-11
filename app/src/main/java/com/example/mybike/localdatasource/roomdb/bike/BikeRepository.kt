package com.example.mybike.localdatasource.roomdb.bike

import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import com.example.mybike.localdatasource.roomdb.relationships.BikeWithRides
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BikeRepository @Inject constructor(private val myBikeDataBase: MyBikeDataBase) : IBikeRepository {
    override suspend fun addBike(bikeEntity: BikeEntity): Long {
        return myBikeDataBase.bikeDao().addBike(bikeEntity)
    }

    override suspend fun updateBike(bikeEntity: BikeEntity): Int {
        return myBikeDataBase.bikeDao().updateBike(bikeEntity)
    }

    override suspend fun updateTraveledDistance(bikeId: Long, newDistance: Int): Int {
        return myBikeDataBase.bikeDao().updateTraveledDistance(bikeId, newDistance)
    }

    override suspend fun deleteBike(bikeEntity: BikeEntity): Int {
        return myBikeDataBase.bikeDao().deleteBike(bikeEntity)
    }

    override fun getAllBikes(): Flow<List<BikeEntity>> {
        return myBikeDataBase.bikeDao().getAllBikes()
    }

    override suspend fun getBike(bikeId: Long): BikeEntity? {
        return myBikeDataBase.bikeDao().getBike(bikeId)
    }

    override suspend fun getBikeWithRides(bikeId: Long): BikeWithRides {
        return myBikeDataBase.bikeDao().getBikeWithRides(bikeId)
    }


}