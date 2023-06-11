package com.example.mybike.repository

import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.localdatasource.roomdb.bike.BikeRepository
import com.example.mybike.localdatasource.roomdb.relationships.BikeWithRides
import com.example.mybike.localdatasource.roomdb.ride.RideEntity
import com.example.mybike.localdatasource.roomdb.ride.RideRepository
import com.example.mybike.localdatasource.sharedpreferences.MyBikeSharedPreferences
import com.example.mybike.vo.BikeToShow
import com.example.mybike.vo.DistanceUnit
import com.example.mybike.vo.WheelSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseRepository @Inject constructor(
    private val myBikeSharedPreferences: MyBikeSharedPreferences,
    private val bikeRepository: BikeRepository,
    private val rideRepository: RideRepository,
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

    fun getDefaultBike(): String {
        defaultCoroutine.launch {
            defaultBike = bikeRepository.getBike(myBikeSharedPreferences.getDefaultBike())?.bikeName ?: ""
        }
        return defaultBike
    }

    fun getAllBikes(): Flow<List<BikeEntity>> {
        return bikeRepository.getAllBikes()
    }

    fun getAllRides(): Flow<List<RideEntity>> {
        return rideRepository.getAllRides()
    }

    fun addBike(bikeToShow: BikeToShow, bikeName: String, wheelSize: WheelSize, color: Int, serviceDue: Int, isDefaultBike: Boolean) {
        defaultCoroutine.launch {
            val bikeId = bikeRepository.addBike(
                BikeEntity(
                    bikeName = bikeName,
                    bikeColor = color,
                    bikeType = bikeToShow.bikeType,
                    inchWheelSize = wheelSize.value,
                    distanceServiceDueInKm = serviceDue
                )
            )

            if (isDefaultBike && bikeId >= MINIMUM_BIKE_ID) {
                saveDefaultBike(bikeId)
            }
        }
    }

    fun addRide(bikeId: Long, rideTitle: String, distance: Int, duration: Int, date: String) {
        defaultCoroutine.launch {
            rideRepository.addRide(
                RideEntity(
                    associatedBikeId = bikeId,
                    rideTitle = rideTitle,
                    distanceInKm = distance,
                    durationInMinutes = duration,
                    date = date
                )
            )
        }
    }

    suspend fun getBike(bikeId: Long): BikeEntity? {
        return bikeRepository.getBike(bikeId)
    }

    suspend fun getBikeWithRides(bikeId: Long): BikeWithRides {
        return bikeRepository.getBikeWithRides(bikeId)
    }

    fun deleteBike(bikeEntity: BikeEntity) {
        defaultCoroutine.launch {
            val bikeWithRides = bikeRepository.getBikeWithRides(bikeEntity.bikeId)
            bikeWithRides.rides.forEach {
                rideRepository.deleteRide(it)
            }
            bikeRepository.deleteBike(bikeEntity)
        }
    }

    suspend fun deleteRide(rideEntity: RideEntity) {
        rideRepository.deleteRide(rideEntity)
    }
}