package com.example.mybike.localdatasource.roomdb

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mybike.localdatasource.roomdb.bike.BikeDao
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.localdatasource.roomdb.ride.RideDao
import com.example.mybike.localdatasource.roomdb.ride.RideEntity

@Database(version = 1, exportSchema = false, entities = [BikeEntity::class, RideEntity::class])
@TypeConverters(Converters::class)
abstract class MyBikeDataBase : RoomDatabase() {

    abstract fun bikeDao(): BikeDao
    abstract fun rideDao(): RideDao

    companion object {
        private const val TAG = "[LocalDataSource]: MyBikeDataBase"
        private const val DATA_BASE_NAME = "MyBike.db"

        @Volatile
        private var instance: MyBikeDataBase? = null

        fun getInstance(context: Context): MyBikeDataBase {
            return instance ?: synchronized(this) {
                instance ?: initializeDataBase(context).also {
                    instance = it
                }
            }
        }

        private fun initializeDataBase(context: Context): MyBikeDataBase {
            Log.i(TAG, "Initialize My Bike Data Base")
            return Room
                .databaseBuilder(context, MyBikeDataBase::class.java, DATA_BASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}