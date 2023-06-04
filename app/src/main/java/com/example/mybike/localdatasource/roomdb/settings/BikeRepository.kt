package com.example.mybike.localdatasource.roomdb.settings

import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import javax.inject.Inject

class BikeRepository @Inject constructor(private val myBikeDataBase: MyBikeDataBase) : IBikeRepository {
    override fun addBike(bikeEntity: BikeEntity): Long {
        return myBikeDataBase.bikeDao().addBike(bikeEntity)
    }

    override fun updateBike(bikeEntity: BikeEntity): Int {
        return myBikeDataBase.bikeDao().updateBike(bikeEntity)
    }

    override fun deleteBike(bikeEntity: BikeEntity): Int {
        return myBikeDataBase.bikeDao().deleteBike(bikeEntity)
    }
}