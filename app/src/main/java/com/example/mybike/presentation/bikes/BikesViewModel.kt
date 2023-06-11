package com.example.mybike.presentation.bikes

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.repository.BaseRepository
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.toWheelSize
import com.example.mybike.vo.BikeToShow
import com.example.mybike.vo.WheelSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikesViewModel @Inject constructor(private val baseRepository: BaseRepository) : ViewModel() {

    companion object {
        private const val TAG = "[Bikes]: BikesViewModel"
    }

    private val _bikesList = MutableStateFlow(emptyList<BikeEntity>())
    val bikesList: StateFlow<List<BikeEntity>> = _bikesList

    private val _currentSelectedBikeToShow = MutableStateFlow<BikeToShow>(BikeToShow.RoadBike)
    val currentSelectedBikeToShow: StateFlow<BikeToShow> = _currentSelectedBikeToShow

    private val _currentColor = MutableStateFlow<Color>(White)
    val currentColor: StateFlow<Color> = _currentColor

    private val _currentWheelSize = MutableStateFlow(WheelSize.BIG)
    val currentWheelSize: StateFlow<WheelSize> = _currentWheelSize

    val currentDistanceUnit = baseRepository.getSettingsDistanceUnit()

    fun getBikes() {
        viewModelScope.launch {
            baseRepository.getAllBikes().collect {
                _bikesList.value = it
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

    fun deleteBike(bikeEntity: BikeEntity) {
        baseRepository.deleteBike(bikeEntity)
    }
}