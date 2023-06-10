package com.example.mybike.presentation.rides

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.repository.BaseRepository
import com.example.mybike.utils.toMinutes
import com.example.mybike.vo.DisplayRideItem
import com.example.mybike.vo.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRideViewModel @Inject constructor(
    private val baseRepository: BaseRepository
): ViewModel() {

    companion object {
        private const val TAG = "[Rides]: AddRideViewModel"
    }

    val currentDistanceUnit = baseRepository.getSettingsDistanceUnit()

    private var duration: Time = Time(0, 0)

    private val _bikesList = MutableStateFlow(emptyList<BikeEntity>())
    val bikesList: StateFlow<List<BikeEntity>> = _bikesList

    fun getBikes() {
        viewModelScope.launch {
            baseRepository.getAllBikes().collect {
                _bikesList.value = it
            }
        }
    }

    fun saveDuration(time: Time) {
        duration = time
    }

    fun addRide(bikeName: String, rideTitle: String, distance: String, date: String) {
        val bikeId = _bikesList.value.firstOrNull { it.bikeName == bikeName }?.bikeId
        val distanceIntegerValue = try {
            distance.toInt()
        } catch (exception: NumberFormatException) {
            Log.e(TAG, exception.message.toString())
            0
        }
        bikeId?.let {
            baseRepository.addRide(
                it,
                rideTitle,
                distanceIntegerValue,
                duration.toMinutes(),
                date
            )
        }
    }
}