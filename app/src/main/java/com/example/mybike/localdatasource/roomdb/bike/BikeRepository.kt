package com.example.mybike.localdatasource.roomdb.bike

import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BikeRepository @Inject constructor(private val myBikeDataBase: MyBikeDataBase) : IBikeRepository {
    override suspend fun addBike(bikeEntity: BikeEntity): Long {
        return myBikeDataBase.bikeDao().addBike(bikeEntity)
    }

    override suspend fun updateBike(bikeEntity: BikeEntity): Int {
        return myBikeDataBase.bikeDao().updateBike(bikeEntity)
    }

    override suspend fun deleteBike(bikeEntity: BikeEntity): Int {
        return myBikeDataBase.bikeDao().deleteBike(bikeEntity)
    }

    override suspend fun getAllBikes(): Flow<List<BikeEntity>> {
        return myBikeDataBase.bikeDao().getAllBikes()
    }

    override suspend fun getBike(bikeId: Long): BikeEntity {
        return myBikeDataBase.bikeDao().getBike(bikeId)
    }
}