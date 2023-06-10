package com.example.mybike.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.mybike.vo.DistanceUnit
import com.example.mybike.vo.Time
import com.example.mybike.vo.TimeType
import com.example.mybike.vo.WheelSize
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date

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

fun String.toTime(type: TimeType): Int {
    if (type == TimeType.HOUR) {
        return try {
            this.toInt()
        } catch (exception: NumberFormatException) {
            Log.e(TAG, "toTime failed: ${exception.message}")
            0
        }
    }
    return try {
        if (this.toInt() < 60) this.toInt() else 59
    } catch (exception: NumberFormatException) {
        Log.e(TAG, "toTime failed: ${exception.message}")
        0
    }
}

fun Time.toMinutes(): Int = hour * 60 + minute
fun Int.toTime(): Time = Time(this / 60, this % 60)

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(DURATION_FORMAT))
    } catch (exception: DateTimeParseException) {
        Log.e(TAG, exception.message.toString())
        null
    }
}