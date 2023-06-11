package com.example.mybike.localdatasource.roomdb.bike

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mybike.localdatasource.roomdb.relationships.BikeWithRides
import kotlinx.coroutines.flow.Flow

@Dao
interface BikeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addBike(bikeEntity: BikeEntity): Long

    @Update
    fun updateBike(bikeEntity: BikeEntity): Int

    @Query("UPDATE bikes SET traveledDistanceInKm = :newTraveledDistance WHERE bikeId= :bikeId")
    fun updateTraveledDistance(bikeId: Long, newTraveledDistance: Int): Int

    @Delete
    fun deleteBike(bikeType: BikeEntity): Int

    @Query("SELECT * FROM bikes")
    fun getAllBikes(): Flow<List<BikeEntity>>

    @Query("SELECT * FROM bikes WHERE bikeId= :bikeId")
    fun getBike(bikeId: Long): BikeEntity?

    @Transaction
    @Query("SELECT * FROM bikes WHERE bikeId= :bikeId")
    fun getBikeWithRides(bikeId: Long): BikeWithRides

}