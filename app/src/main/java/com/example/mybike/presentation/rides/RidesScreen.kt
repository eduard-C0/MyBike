package com.example.mybike.presentation.rides

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybike.presentation.CustomButton
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.R
import com.example.mybike.presentation.CustomThreeDotsDropdown
import com.example.mybike.presentation.TopBarAddAction
import com.example.mybike.presentation.bikes.ItemCharacteristics
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.HOUR_SUFFIX
import com.example.mybike.utils.MINUTES_SUFFIX
import com.example.mybike.utils.toTime
import com.example.mybike.vo.DisplayRideItem
import com.example.mybike.vo.ThreeDotsDropdownItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RidesScreen(ridesViewModel: RidesViewModel, onAddRideClicked: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        ridesViewModel.getRides()
    }
    val ridesList = ridesViewModel.ridesList.collectAsState()

    if (ridesList.value.isEmpty()) {
        EmptyRidesScreen { onAddRideClicked() }

    } else {
        ContentRideScree(onAddRideClicked, ridesList.value, ridesViewModel)
    }

}

@Composable
fun EmptyRidesScreen(onAddRideClicked: () -> Unit) {
    Column(
        Modifier
            .background(Black)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.rides_screen))
        Image(painter = painterResource(id = R.drawable.missing_rides_card), contentDescription = null, modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d12)))
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.dotted_line_rides),
                contentDescription = null,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.d40), top = dimensionResource(id = R.dimen.d12)),
                tint = LightBlue
            )
        }

        CustomButton(
            text = stringResource(id = R.string.add_ride), enabled = true, onClick = onAddRideClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.d12)),
        )
    }
}

@Composable
fun ContentRideScree(onAddRideClicked: () -> Unit, rideList: List<DisplayRideItem>, ridesViewModel: RidesViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Black),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.rides_screen), actions = { TopBarAddAction(onAddRideClicked, stringResource(id = R.string.add_ride)) })

        LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d8))) {

            items(rideList.size) {
                when (val ride = rideList[it]) {
                    is DisplayRideItem.Divider -> {
                        Text(text = ride.text, color = Gray, modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.d8)))
                    }

                    is DisplayRideItem.RideItemData -> {
                        RideItem(ride, ridesViewModel)
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d12)))
            }
        }
    }
}



@Composable
fun RideItem(rideEntity: DisplayRideItem.RideItemData, ridesViewModel: RidesViewModel) {
    val duration = rideEntity.durationInMinutes.toTime()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.d4)))
            .background(BackgroundColor)
            .padding(horizontal = dimensionResource(id = R.dimen.d8), vertical = dimensionResource(id = R.dimen.d4)), horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = rideEntity.rideTitle ?: "", color = White, style = Typography.titleMedium)

            CustomThreeDotsDropdown(
                elements = listOf(
                    ThreeDotsDropdownItem("Edit", R.drawable.icon_edit, White, {}, White, Typography.titleSmall),
                    ThreeDotsDropdownItem("Delete", R.drawable.icon_delete, White, {}, White, Typography.titleSmall)
                )
            )
        }
        ItemCharacteristics(title = stringResource(id = R.string.bike) + stringResource(id = R.string.duration_separator), value = rideEntity.bikeName)
        ItemCharacteristics(
            title = stringResource(id = R.string.distance) + stringResource(id = R.string.duration_separator),
            value = rideEntity.distanceInKm.toString() + ridesViewModel.currentDistanceUnit
        )
        ItemCharacteristics(
            title = stringResource(id = R.string.duration) + stringResource(id = R.string.duration_separator),
            value = duration.hour.toString() + HOUR_SUFFIX + duration.minute.toString() + MINUTES_SUFFIX
        )
        ItemCharacteristics(title = stringResource(id = R.string.date) + stringResource(id = R.string.duration_separator), value = rideEntity.date)
    }
}

@Preview
@Composable
fun PreviewEmptyRidesScreen() {
    EmptyRidesScreen {
        TODO()
    }
}