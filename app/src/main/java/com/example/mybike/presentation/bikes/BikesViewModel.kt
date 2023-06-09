package com.example.mybike.presentation.bikes

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.repository.BaseRepository
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.toBikeEntity
import com.example.mybike.utils.toWheelSize
import com.example.mybike.vo.BikeDataItem
import com.example.mybike.vo.BikeToShow
import com.example.mybike.vo.WheelSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikesViewModel @Inject constructor(
    private val baseRepository: BaseRepository,
    private val defaultCoroutine: CoroutineScope
) : ViewModel() {

    companion object {
        private const val TAG = "[Bikes]: BikesViewModel"
    }

    private val _bikesList = MutableStateFlow(emptyList<BikeDataItem>())
    val bikesList: StateFlow<List<BikeDataItem>> = _bikesList

    private val _currentSelectedBikeToShow = MutableStateFlow<BikeToShow>(BikeToShow.RoadBike)
    val currentSelectedBikeToShow: StateFlow<BikeToShow> = _currentSelectedBikeToShow

    private val _currentColor = MutableStateFlow<Color>(White)
    val currentColor: StateFlow<Color> = _currentColor

    private val _currentWheelSize = MutableStateFlow(WheelSize.BIG)
    val currentWheelSize: StateFlow<WheelSize> = _currentWheelSize

    val currentDistanceUnit = baseRepository.getSettingsDistanceUnit()

    fun getBikes() {
        defaultCoroutine.launch {
            baseRepository.getAllBikes().collect {
                _bikesList.value = it.map { bikeEntity ->
                    BikeDataItem(
                        bikeEntity.bikeId,
                        bikeEntity.bikeName,
                        bikeEntity.bikeType,
                        bikeEntity.inchWheelSize,
                        bikeEntity.bikeColor,
                        bikeEntity.distanceServiceDueInKm,
                        baseRepository.getBikeWithRides(bikeEntity.bikeId).rides.sumOf { ride -> ride.distanceInKm }
                    )
                }
            }
        }
    }

    fun saveCurrentColor(color: Color) {
        _currentColor.value = color
    }

    fun saveCurrentBike(bikeToShow: BikeToShow) {
        _currentSelectedBikeToShow.value = bikeToShow
    }

    fun saveWheelSize(wheelSize: String) {
        _currentWheelSize.value = wheelSize.toWheelSize()
    }

    fun addBike(isDefaultBike: Boolean, serviceDue: String, bikeName: String) {
        try {
            baseRepository.addBike(
                bikeToShow = _currentSelectedBikeToShow.value,
                bikeName = bikeName,
                color = _currentColor.value.toArgb(),
                wheelSize = _currentWheelSize.value,
                isDefaultBike = isDefaultBike,
                serviceDue = serviceDue.toInt()
            )
        } catch (exception: NumberFormatException) {
            Log.e(TAG, exception.message.toString())
        }
    }

    fun deleteBike(bikeEntity: BikeDataItem) {
        baseRepository.deleteBike(bikeEntity.toBikeEntity())
    }
}