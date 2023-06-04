package com.example.mybike.localdatasource.roomdb.settings

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BikeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addBike(bikeEntity: BikeEntity): Long

    @Update
    fun updateBike(bikeEntity: BikeEntity): Int

    @Delete
    fun deleteBike(bikeType: BikeEntity): Int
}