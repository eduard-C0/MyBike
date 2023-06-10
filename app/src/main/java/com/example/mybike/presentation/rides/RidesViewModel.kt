package com.example.mybike.presentation.rides

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.mybike.repository.BaseRepository
import com.example.mybike.utils.toLocalDate
import com.example.mybike.vo.DisplayRideItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRides() {
        defaultCoroutine.launch {
            baseRepository.getAllRides().collect { rideList ->
                val map = rideList.map {
                    DisplayRideItem.RideItemData(
                        it.rideId,
                        it.associatedBikeId,
                        it.rideTitle,
                        it.distanceInKm,
                        it.durationInMinutes,
                        it.date,
                        baseRepository.getBike(it.associatedBikeId).bikeName
                    )
                }
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


}