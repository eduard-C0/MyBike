package com.example.mybike.localdatasource.roomdb.settings

interface IBikeRepository {

    fun addBike(bikeEntity: BikeEntity): Long

    fun updateBike(bikeEntity: BikeEntity): Int

    fun deleteBike(bikeEntity: BikeEntity): Int
}