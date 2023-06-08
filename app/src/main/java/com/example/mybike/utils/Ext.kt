package com.example.mybike.utils

import android.util.Log
import com.example.mybike.vo.DistanceUnit
import com.example.mybike.vo.WheelSize

private const val TAG = "[Utils]: Ext"

fun String.toDistanceUnit(): DistanceUnit {
    return when (this) {
        DistanceUnit.KM.name -> {
            DistanceUnit.KM
        }

        DistanceUnit.MI.name -> {
            DistanceUnit.MI
        }

        else -> {
            Log.e(TAG, "toDistanceUnit failed")
            DistanceUnit.KM
        }
    }
}

fun String.toWheelSize(): WheelSize {
    return when (this) {
        WheelSize.BIG.value -> {
            WheelSize.BIG
        }

        WheelSize.SMALL.value -> {
            WheelSize.SMALL
        }
        else -> {
            Log.e(TAG, "toWheelSize failed")
            WheelSize.BIG
        }
    }
}