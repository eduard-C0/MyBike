package com.example.mybike.vo

enum class WheelSize(var value: String) {
    BIG("29\""), SMALL("28\"")
}

fun String.toWheelSize(): WheelSize {
    return when (this) {
        WheelSize.BIG.value -> WheelSize.BIG
        WheelSize.SMALL.value -> WheelSize.SMALL
        else -> {
            WheelSize.BIG
        }
    }
}