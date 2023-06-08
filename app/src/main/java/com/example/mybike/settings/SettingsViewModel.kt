package com.example.mybike.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.repository.BaseRepository
import com.example.mybike.utils.toDistanceUnit
import com.example.mybike.vo.DistanceUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val baseRepository: BaseRepository) : ViewModel() {
    companion object {
        private const val TAG = "[Settings]: SettingsViewModel"
        private const val DEFAULT_SERVICE_REMINDER_DISTANCE = 100
    }

    private val _distanceUnit = MutableStateFlow(DistanceUnit.KM)
    val distanceUnit: StateFlow<DistanceUnit> = _distanceUnit

    private val _bikeList = MutableStateFlow<List<BikeEntity>>(emptyList())
    val bikeList: StateFlow<List<BikeEntity>> = _bikeList

    private val _serviceReminderDistance = MutableStateFlow(DEFAULT_SERVICE_REMINDER_DISTANCE)
    val serviceReminderDistance: StateFlow<Int> = _serviceReminderDistance

    private val _defaultBike = MutableStateFlow("")
    val defaultBike: StateFlow<String> = _defaultBike

    private val _serviceReminderNotificationStatus = MutableStateFlow(false)
    val serviceReminderNotificationStatus: StateFlow<Boolean> = _serviceReminderNotificationStatus

    fun getSettingsDistanceUnit() {
        _distanceUnit.value = baseRepository.getSettingsDistanceUnit()
    }

    fun getServiceReminderDistance() {
        _serviceReminderDistance.value = baseRepository.getSettingsServiceReminderDistance()
    }

    fun getDefaultBike() {
        viewModelScope.launch {
            _defaultBike.value = baseRepository.getDefaultBike()
        }
    }

    fun getServiceReminderNotificationStatus() {
        _serviceReminderNotificationStatus.value = baseRepository.getServiceReminderNotificationStatus()
    }

    fun saveServiceReminderNotificationStatus(status: Boolean) {
        baseRepository.saveShowServiceReminder(status)
        _serviceReminderNotificationStatus.value = status
    }

    fun saveDistanceUnit(value: String) {
        baseRepository.saveSettingsDistanceUnit(value.toDistanceUnit())
        _distanceUnit.value = value.toDistanceUnit()
    }

    fun saveServiceReminderDistance(value: String) {
        try {
            val integerValue = value.toInt()
            baseRepository.saveSettingsServiceReminder(integerValue)
            _serviceReminderDistance.value = integerValue
        } catch (exception: NumberFormatException) {
            Log.e(TAG, "${exception.message}")
        }
    }

    fun getBikes() {
        viewModelScope.launch {
            baseRepository.getAllBikes().collect {
                _bikeList.value = it
            }
        }
    }
}