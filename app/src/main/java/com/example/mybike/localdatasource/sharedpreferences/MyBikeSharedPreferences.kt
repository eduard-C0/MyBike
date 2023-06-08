package com.example.mybike.localdatasource.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.mybike.vo.DistanceUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MyBikeSharedPreferences @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val TAG = "[LocalDataSource]: MyBikeSharedPreferences"
        private const val SHARED_PREFERENCES_NAME = "my_bike_config"
        private const val SHARED_PREFERENCES_ERROR = "SharedPreferences Error"
        private const val DISTANCE_UNIT_KEY = "distance_unit"
        private const val SERVICE_REMINDER_DISTANCE_KEY = "service_reminder_distance"
        private const val SHOW_SERVICE_REMINDER_KEY = "show_service_reminder"
        private const val DEFAULT_BIKE_NAME_KEY = "default_bike_name"
        private const val DEFAULT_SERVICE_REMINDER_DISTANCE = 100

    }

    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            SHARED_PREFERENCES_NAME,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getValue(key: String, default: Any): Any? = with(sharedPreferences) {
        return when (default) {
            is Int -> getInt(key, default)
            is String -> getString(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            is Boolean -> getBoolean(key, default)
            else -> throw IllegalArgumentException(SHARED_PREFERENCES_ERROR)
        }
    }

    private fun saveValue(key: String, value: Any?) = with(sharedPreferences.edit()) {
        if (value == null) {
            remove(key)
        } else {
            when (value) {
                is Long -> putLong(key, value)
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw IllegalArgumentException(SHARED_PREFERENCES_ERROR)
            }.apply()
        }
    }

    fun getString(key: String, default: String = ""): String {
        return getValue(key, default) as String
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getValue(key, default) as Int
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getValue(key, default) as Long
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getValue(key, default) as Boolean
    }

    fun saveDistanceUnits(distanceUnit: DistanceUnit) {
        saveValue(DISTANCE_UNIT_KEY, distanceUnit.name)
    }

    fun saveServiceReminderDistance(serviceReminderDistance: Int) {
        saveValue(SERVICE_REMINDER_DISTANCE_KEY, serviceReminderDistance)
    }

    fun saveServiceReminderNotificationStatus(showServiceReminder: Boolean) {
        saveValue(SHOW_SERVICE_REMINDER_KEY, showServiceReminder)
    }

    fun saveDefaultBike(defaultBikeId: Long) {
        saveValue(DEFAULT_BIKE_NAME_KEY, defaultBikeId)
    }

    fun getDistanceUnits(): DistanceUnit {
        return when (getString(DISTANCE_UNIT_KEY, DistanceUnit.KM.name)) {
            DistanceUnit.KM.name -> {
                DistanceUnit.KM
            }

            DistanceUnit.MI.name -> {
                DistanceUnit.MI
            }

            else -> {
                Log.e(TAG, "getDistanceUnits failed")
                DistanceUnit.KM
            }
        }
    }

    fun getServiceReminderDistance(): Int {
        return getInt(SERVICE_REMINDER_DISTANCE_KEY, DEFAULT_SERVICE_REMINDER_DISTANCE)
    }

    fun getServiceReminderNotificationStatus(): Boolean {
        return getBoolean(SHOW_SERVICE_REMINDER_KEY, false)
    }

    fun getDefaultBike(): Long {
        return getLong(DEFAULT_BIKE_NAME_KEY)
    }

}
