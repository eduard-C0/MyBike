package com.example.mybike.localdatasource.roomdb.bike

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mybike.vo.BikeType

@Entity(tableName = "bikes")
data class BikeEntity(
    @PrimaryKey(autoGenerate = true) val bikeId: Long = 0,
    val bikeName: String,
    val bikeType: BikeType,
    val inchWheelSize: String,
    val bikeColor: Int,
    val distanceServiceDueInKm: Int
)