package com.example.mybike.localdatasource.roomdb.ride

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addRide(rideEntity: RideEntity): Long

    @Update
    fun updateRide(rideEntity: RideEntity): Int

    @Delete
    fun deleteRide(rideEntity: RideEntity): Int

    @Query("SELECT * FROM rides")
    fun getAllRides(): Flow<List<RideEntity>>

    @Query("SELECT * FROM rides WHERE rideId= :rideId")
    fun getBike(rideId: Long): RideEntity?
}