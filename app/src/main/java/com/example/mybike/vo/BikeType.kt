package com.example.mybike.vo

enum class BikeType(val textToShow: String) {
    MTB("MTB"), ROADBIKE("Road Bike"), ELECTRIC("Electric"), HYBRID("Hybrid")
}

fun BikeType.toBike(): Bike {
    return when (this) {
        BikeType.MTB -> Bike.MtbBike
        BikeType.HYBRID -> Bike.HybridBike
        BikeType.ELECTRIC -> Bike.ElectricBike
        BikeType.ROADBIKE -> Bike.RoadBike
    }
}