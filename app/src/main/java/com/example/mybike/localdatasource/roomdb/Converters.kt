package com.example.mybike.localdatasource.roomdb

import androidx.room.TypeConverter
import com.example.mybike.vo.BikeType

class Converters {

    @TypeConverter
    fun fromBikeType(bikeType: BikeType): String {
        return bikeType.name
    }

    @TypeConverter
    fun toBikeType(bikeType: String): BikeType {
        return BikeType.valueOf(bikeType)
    }
}