package com.example.mybike.vo

import androidx.annotation.DrawableRes
import com.example.mybike.R

sealed class BikeToShow {
    abstract val bikeType: BikeType

    @get:DrawableRes
    abstract val middle: Int

    @get:DrawableRes
    abstract val over: Int

    @get:DrawableRes
    abstract val smallWheel: Int

    @get:DrawableRes
    abstract val bigWheel: Int

    object MtbBike : BikeToShow() {
        override val bikeType: BikeType
            get() = BikeType.MTB
        override val middle: Int
            get() = R.drawable.bike_mtb_middle
        override val over: Int
            get() = R.drawable.bike_mtb_over
        override val smallWheel: Int
            get() = R.drawable.bike_mtb_small_wheels
        override val bigWheel: Int
            get() = R.drawable.bike_mtb_big_wheels
    }

    object RoadBike : BikeToShow() {
        override val bikeType: BikeType
            get() = BikeType.ROADBIKE
        override val middle: Int
            get() = R.drawable.bike_roadbike_middle
        override val over: Int
            get() = R.drawable.bike_roadbike_over
        override val smallWheel: Int
            get() = R.drawable.bike_roadbike_small_wheels
        override val bigWheel: Int
            get() = R.drawable.bike_roadbike_big_wheels
    }

    object ElectricBike : BikeToShow() {
        override val bikeType: BikeType
            get() = BikeType.ELECTRIC
        override val middle: Int
            get() = R.drawable.bike_electric_middle
        override val over: Int
            get() = R.drawable.bike_electric_over
        override val smallWheel: Int
            get() = R.drawable.bike_electric_small_wheels
        override val bigWheel: Int
            get() = R.drawable.bike_electric_big_wheels

    }

    object HybridBike : BikeToShow() {
        override val bikeType: BikeType
            get() = BikeType.HYBRID
        override val middle: Int
            get() = R.drawable.bike_hybrid_middle
        override val over: Int
            get() = R.drawable.bike_hybrid_over
        override val smallWheel: Int
            get() = R.drawable.bike_hybrid_small_wheels
        override val bigWheel: Int
            get() = R.drawable.bike_hybrid_big_wheels

    }
}
