package com.example.mybike.localdatasource.roomdb.relationships

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.localdatasource.roomdb.ride.RideEntity

@Entity
data class BikeWithRides(
    @Embedded val bike: BikeEntity,
    @Relation(
        parentColumn = "bikeId",
        entityColumn = "associatedBikeId"
    )
    val rides: List<RideEntity>
)
