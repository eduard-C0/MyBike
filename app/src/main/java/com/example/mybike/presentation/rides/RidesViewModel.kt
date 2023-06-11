package com.example.mybike.presentation.rides

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.mybike.repository.BaseRepository
import com.example.mybike.utils.toLocalDate
import com.example.mybike.utils.toRideEntity
import com.example.mybike.vo.BikeRideWithBike
import com.example.mybike.vo.BikeType
import com.example.mybike.vo.DisplayRideItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RidesViewModel @Inject constructor(
    private val baseRepository: BaseRepository,
    private val defaultCoroutine: CoroutineScope
) : ViewModel() {

    companion object {
        private const val TAG = "[Rides]: RidesViewModel"
    }

    val currentDistanceUnit = baseRepository.getSettingsDistanceUnit()

    private val _ridesList = MutableStateFlow(emptyList<DisplayRideItem>())
    val ridesList: StateFlow<List<DisplayRideItem>> = _ridesList

    private val _rides = MutableStateFlow(emptyList<BikeRideWithBike>())

    fun getDistanceForType(bikeType: BikeType, coroutineScope: CoroutineScope): StateFlow<Int> =
        _rides.map { it ->
            it.filter { ride -> ride.bike.bikeType == bikeType }.sumOf { it.distanceInKm }
        }.stateIn(
            scope = coroutineScope, started = SharingStarted.Lazily,
            initialValue = 0
        )

    fun getTotalDistance(coroutineScope: CoroutineScope): StateFlow<Int> =
        _rides.map { it ->
            it.sumOf { it.distanceInKm }
        }.stateIn(
            scope = coroutineScope, started = SharingStarted.Lazily,
            initialValue = 0
        )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRides() {
        defaultCoroutine.launch {
            baseRepository.getAllRides().collect { rideList ->
                val mappedRideList = mutableListOf<DisplayRideItem.RideItemData>()
                val ridesWithBikesList = mutableListOf<BikeRideWithBike>()
                rideList.forEach {
                    val associatedBike = baseRepository.getBike(it.associatedBikeId)
                    associatedBike?.let { bikeEntity ->
                        mappedRideList.add(
                            DisplayRideItem.RideItemData(
                                it.rideId,
                                it.associatedBikeId,
                                it.rideTitle,
                                it.distanceInKm,
                                it.durationInMinutes,
                                it.date,
                                bikeEntity.bikeName
                            )
                        )
                        ridesWithBikesList.add(
                            BikeRideWithBike(
                                it.rideId,
                                it.associatedBikeId,
                                it.rideTitle,
                                it.distanceInKm,
                                it.durationInMinutes,
                                it.date,
                                bikeEntity
                            )
                        )
                    }
                }

                _rides.value = ridesWithBikesList

                val map = mappedRideList
                    .sortedBy { it.date.toLocalDate() }
                    .groupBy { it.date.toLocalDate()?.month?.name }

                val groupedByListOfDisplayRideItem = mutableListOf<DisplayRideItem>()

                map.keys.forEach { key ->
                    key?.let {
                        groupedByListOfDisplayRideItem.add(DisplayRideItem.Divider(it))
                        map[key]?.let { values -> groupedByListOfDisplayRideItem.addAll(values) }
                    }
                }
                _ridesList.value = groupedByListOfDisplayRideItem
            }
        }
    }

    fun deleteRide(displayRideItem: DisplayRideItem.RideItemData) {
        baseRepository.deleteRide(displayRideItem.toRideEntity())
    }


}