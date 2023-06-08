package com.example.mybike.repository

import com.example.mybike.localdatasource.roomdb.MyBikeDataBase
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.localdatasource.roomdb.bike.BikeRepository
import com.example.mybike.localdatasource.sharedpreferences.MyBikeSharedPreferences
import com.example.mybike.vo.Bike
import com.example.mybike.vo.DistanceUnit
import com.example.mybike.vo.WheelSize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseRepository @Inject constructor(
    private val myBikeSharedPreferences: MyBikeSharedPreferences,
    private val myBikeRepository: BikeRepository,
    private val defaultCoroutine: CoroutineScope
) {

    companion object {
        private const val MINIMUM_BIKE_ID = 0
    }

    private var defaultBike: String = ""

    fun saveSettingsDistanceUnit(distanceUnit: DistanceUnit) {
        myBikeSharedPreferences.saveDistanceUnits(distanceUnit)
    }

    fun getSettingsDistanceUnit(): DistanceUnit {
        return myBikeSharedPreferences.getDistanceUnits()
    }

    fun saveSettingsServiceReminder(serviceReminderDistance: Int) {
        myBikeSharedPreferences.saveServiceReminderDistance(serviceReminderDistance)
    }

    fun getSettingsServiceReminderDistance(): Int {
        return myBikeSharedPreferences.getServiceReminderDistance()
    }

    fun saveShowServiceReminder(showServiceReminder: Boolean) {
        myBikeSharedPreferences.saveServiceReminderNotificationStatus(showServiceReminder)
    }

    fun getServiceReminderNotificationStatus(): Boolean {
        return myBikeSharedPreferences.getServiceReminderNotificationStatus()
    }

    fun saveDefaultBike(defaultBikeId: Long) {
        myBikeSharedPreferences.saveDefaultBike(defaultBikeId)
    }

    suspend fun getDefaultBike(): String {
        defaultCoroutine.launch {
            defaultBike = myBikeRepository.getBike(myBikeSharedPreferences.getDefaultBike()).bikeName
        }
        return defaultBike
    }

    suspend fun getAllBikes(): Flow<List<BikeEntity>> {
        return myBikeRepository.getAllBikes()
    }

    fun addBike(bike: Bike, bikeName: String, wheelSize: WheelSize, color: Int, serviceDue: Int, isDefaultBike: Boolean) {
        defaultCoroutine.launch {
            val bikeId = myBikeRepository.addBike(
                BikeEntity(
                    bikeName = bikeName,
                    bikeColor = color,
                    bikeType = bike.bikeType,
                    inchWheelSize = wheelSize.value,
                    distanceServiceDueInKm = serviceDue
                )
            )

            if (isDefaultBike && bikeId >= MINIMUM_BIKE_ID) {
                saveDefaultBike(bikeId)
            }
        }
    }
}