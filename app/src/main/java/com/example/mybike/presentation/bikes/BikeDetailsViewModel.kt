package com.example.mybike.presentation.bikes

import androidx.lifecycle.ViewModel
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.localdatasource.roomdb.relationships.BikeWithRides
import com.example.mybike.localdatasource.roomdb.ride.RideEntity
import com.example.mybike.repository.BaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikeDetailsViewModel @Inject constructor(
    private val baseRepository: BaseRepository,
    private val defaultCoroutine: CoroutineScope

) : ViewModel() {

    private val _bike = MutableStateFlow<BikeEntity?>(null)
    val bike: StateFlow<BikeEntity?> = _bike

    private val _bikeWithRides = MutableStateFlow<BikeWithRides?>(null)
    val bikeWithRides: StateFlow<BikeWithRides?> = _bikeWithRides

    val currentDistanceUnit = baseRepository.getSettingsDistanceUnit()

    fun getBike(bikeId: Long) {
        defaultCoroutine.launch {
            _bike.value = baseRepository.getBike(bikeId)
        }
    }

    fun getBikeWithRides(bikeId: Long) {
        defaultCoroutine.launch {
            _bikeWithRides.value = baseRepository.getBikeWithRides(bikeId)
        }
    }

    fun deleteRide(rideEntity: RideEntity) {
        defaultCoroutine.launch {
            baseRepository.deleteRide(rideEntity)
            getBikeWithRides(rideEntity.associatedBikeId)
        }
    }

}