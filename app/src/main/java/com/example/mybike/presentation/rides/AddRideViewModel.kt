package com.example.mybike.presentation.rides

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.localdatasource.roomdb.ride.RideEntity
import com.example.mybike.repository.BaseRepository
import com.example.mybike.utils.toMinutes
import com.example.mybike.vo.DisplayRideItem
import com.example.mybike.vo.Time
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class AddRideViewModel @Inject constructor(
    private val baseRepository: BaseRepository,
    private val defaultCoroutine: CoroutineScope
) : ViewModel() {

    companion object {
        private const val TAG = "[Rides]: AddRideViewModel"
    }

    val currentDistanceUnit = baseRepository.getSettingsDistanceUnit()

    private val _bikesList = MutableStateFlow(emptyList<BikeEntity>())
    val bikesList: StateFlow<List<BikeEntity>> = _bikesList

    private val _currentRide = MutableStateFlow<RideEntity?>(null)
    val currentRide: StateFlow<RideEntity?> = _currentRide

    val bikeName: StateFlow<String?> = _bikesList.combine(_currentRide) { bikesList, currentRide ->
        Pair(bikesList, currentRide)
    }.map { pair -> pair.first.firstOrNull { it.bikeId == pair.second?.associatedBikeId }?.bikeName }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = "")

    fun getBikes() {
        viewModelScope.launch {
            baseRepository.getAllBikes().collect {
                _bikesList.value = it
            }
        }
    }

    fun getRide(rideId: Long) {
        defaultCoroutine.launch {
            _currentRide.value = baseRepository.getRide(rideId)
        }
    }

    fun addRide(bikeName: String, rideTitle: String, distance: String, date: String, hour: String, minutes: String) {
        val bikeId = _bikesList.value.firstOrNull { it.bikeName == bikeName }?.bikeId
        try {
            bikeId?.let {
                baseRepository.addRide(
                    it,
                    rideTitle,
                    distance.toInt(),
                    Time(hour.toInt(), minutes.toInt()).toMinutes(),
                    date
                )
            }
        } catch (exception: NumberFormatException) {
            Log.e(TAG, exception.message.toString())
        }

    }

    fun updateRide(rideId: Long, bikeName: String, rideTitle: String, distance: String, date: String, hour: String, minutes: String) {
        defaultCoroutine.launch {
            val bikeId = _bikesList.value.firstOrNull { it.bikeName == bikeName }?.bikeId
            try {
                bikeId?.let {
                    baseRepository.updateRide(
                        RideEntity(
                            rideId,
                            it,
                            rideTitle,
                            distance.toInt(),
                            Time(hour.toInt(), minutes.toInt()).toMinutes(),
                            date
                        )
                    )
                }
            } catch (exception: NumberFormatException) {
                Log.e(TAG, exception.message.toString())
            }

        }
    }
}