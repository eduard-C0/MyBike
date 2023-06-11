package com.example.mybike.presentation.bikes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.mybike.R
import com.example.mybike.presentation.CustomProgressBar
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.presentation.rides.RideItem
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.vo.DisplayRideItem
import com.example.mybike.vo.ThreeDotsDropdownItem
import com.example.mybike.vo.WheelSize
import com.example.mybike.vo.toBike
import com.example.mybike.vo.toWheelSize


@Composable
fun BikeDetailsScreen(bikeId: Long, bikeDetailsViewModel: BikeDetailsViewModel, onBackButtonClicked: () -> Unit, onEditRideClick: (rideId: Long?) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        bikeDetailsViewModel.getBikeWithRides(bikeId)
    }

    val bikeWithRides = bikeDetailsViewModel.bikeWithRides.collectAsState()

    bikeWithRides.value?.let { bikeWithRides ->
        var totalDistance = 0
        bikeWithRides.rides.forEach { ride -> totalDistance += ride.distanceInKm }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            CustomTopBar(title = bikeWithRides.bike.bikeName, customNavigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = White, modifier = Modifier.clickable {
                    onBackButtonClicked()
                })
            }, backgroundColor = BackgroundColor)

            CustomBike(
                bikeToShow = bikeWithRides.bike.bikeType.toBike(),
                color = Color(bikeWithRides.bike.bikeColor),
                withSmallWheel = bikeWithRides.bike.inchWheelSize.toWheelSize() != WheelSize.BIG,
            )
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                ItemCharacteristics(title = stringResource(id = R.string.wheels), value = bikeWithRides.bike.inchWheelSize)
                ItemCharacteristics(
                    title = stringResource(id = R.string.service_in_characteristic),
                    value = bikeWithRides.bike.distanceServiceDueInKm.toString() + bikeDetailsViewModel.currentDistanceUnit.name.lowercase()
                )

                CustomProgressBar(
                    bikeWithRides.rides.sumOf { ride -> ride.distanceInKm } / bikeWithRides.bike.distanceServiceDueInKm.toFloat(),
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.d12))
                )

                ItemCharacteristics(title = stringResource(id = R.string.rides_screen), value = bikeWithRides.rides.size.toString())
                ItemCharacteristics(title = stringResource(id = R.string.total_rides_distance), value = totalDistance.toString() + bikeDetailsViewModel.currentDistanceUnit.name.lowercase())

                Text(
                    text = stringResource(id = R.string.rides_screen).uppercase(),
                    color = LightBlue,
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.d8)),
                    style = Typography.titleMedium
                )
            }


            LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d12))) {
                items(bikeWithRides.rides.size) {
                    val ride = bikeWithRides.rides[it]
                    RideItem(
                        DisplayRideItem.RideItemData(
                            ride.rideId,
                            ride.associatedBikeId,
                            ride.rideTitle,
                            ride.distanceInKm,
                            ride.durationInMinutes,
                            ride.date,
                            bikeWithRides.bike.bikeName,
                        ),
                        bikeDetailsViewModel.currentDistanceUnit,
                        DarkBlue,
                        listOf(
                            ThreeDotsDropdownItem("Edit", R.drawable.icon_edit, White, {onEditRideClick(ride.rideId)}, White, Typography.titleSmall),
                            ThreeDotsDropdownItem("Delete", R.drawable.icon_delete, White, { bikeDetailsViewModel.deleteRide(ride) }, White, Typography.titleSmall)
                        )
                    )


                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d12)))
                }
            }

        }
    }

}