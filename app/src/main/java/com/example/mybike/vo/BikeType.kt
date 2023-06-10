package com.example.mybike.vo

enum class BikeType(val textToShow: String) {
    MTB("MTB"), ROADBIKE("Road Bike"), ELECTRIC("Electric"), HYBRID("Hybrid")
}

fun BikeType.toBike(): BikeToShow {
    return when (this) {
        BikeType.MTB -> BikeToShow.MtbBike
        BikeType.HYBRID -> BikeToShow.HybridBike
        BikeType.ELECTRIC -> BikeToShow.ElectricBike
        BikeType.ROADBIKE -> BikeToShow.RoadBike
    }
}