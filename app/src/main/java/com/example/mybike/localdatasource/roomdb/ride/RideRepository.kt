package com.example.mybike.localdatasource.roomdb.ride

import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RideRepository @Inject constructor(private val myBikeDataBase: MyBikeDataBase) : IRideRepository {

    override suspend fun addRide(rideEntity: RideEntity): Long {
        return myBikeDataBase.rideDao().addRide(rideEntity)
    }

    override suspend fun updateRide(rideEntity: RideEntity): Int {
        return myBikeDataBase.rideDao().updateRide(rideEntity)
    }

    override suspend fun deleteRide(rideEntity: RideEntity): Int {
        return myBikeDataBase.rideDao().deleteRide(rideEntity)
    }

    override fun getAllRides(): Flow<List<RideEntity>> {
        return myBikeDataBase.rideDao().getAllRides()
    }

    override suspend fun getRide(rideId: Long): RideEntity? {
        return myBikeDataBase.rideDao().getBike(rideId)
    }
}