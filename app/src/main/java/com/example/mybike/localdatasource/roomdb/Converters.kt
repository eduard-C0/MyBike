package com.example.mybike.localdatasource.roomdb

import androidx.room.TypeConverter
import com.example.mybike.vo.BikeType

class Converters {

    @TypeConverter
    fun fromDistanceUnit(bikeType: BikeType): String {
        return bikeType.name
    }

    @TypeConverter
    fun toDistanceUnit(bikeType: String): BikeType {
        return BikeType.valueOf(bikeType)
    }
}