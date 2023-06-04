package com.example.mybike.localdatasource.roomdb.settings

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mybike.vo.BikeType

@Entity(tableName = "bikes")
data class BikeEntity(
    @PrimaryKey(autoGenerate = true) val bikeId: Long,
    val bikeName: String,
    val bikeType: BikeType,
    val inchWheelSize: Int,
    val bikeColor: String,
    val distanceServiceDue: Int,
    val traveledDistance: Int,
    val isDefaultBike: Boolean
)