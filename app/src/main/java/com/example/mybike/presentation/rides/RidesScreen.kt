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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybike.presentation.CustomButton
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.R
import com.example.mybike.presentation.CustomThreeDotsDropdown
import com.example.mybike.presentation.TopBarAddAction
import com.example.mybike.presentation.bikes.ItemCharacteristics
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.BikeLime
import com.example.mybike.ui.theme.BikeRed
import com.example.mybike.ui.theme.BikeYellow
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.HOUR_SUFFIX
import com.example.mybike.utils.MINUTES_SUFFIX
import com.example.mybike.utils.toTime
import com.example.mybike.vo.BikeType
import com.example.mybike.vo.DisplayRideItem
import com.example.mybike.vo.DistanceUnit
import com.example.mybike.vo.ThreeDotsDropdownItem
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RidesScreen(ridesViewModel: RidesViewModel, onAddRideClicked: () -> Unit, onEditRideClick: (rideId: Long?) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        ridesViewModel.getRides()
    }
    val ridesList = ridesViewModel.ridesList.collectAsState()

    if (ridesList.value.isEmpty()) {
        EmptyRidesScreen { onAddRideClicked() }
    } else {
        ContentRideScree(onAddRideClicked, ridesList.value, ridesViewModel, onEditRideClick)
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
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = R.drawable.dotted_line_rides),
                contentDescription = null,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.d40), top = dimensionResource(id = R.dimen.d12)),
                tint = LightBlue
            )

            CustomButton(
                text = stringResource(id = R.string.add_ride), enabled = true, onClick = onAddRideClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.d12))
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun ContentRideScree(onAddRideClicked: () -> Unit, rideList: List<DisplayRideItem>, ridesViewModel: RidesViewModel, onEditRideClick: (rideId: Long?) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Black),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.rides_screen), actions = { TopBarAddAction(onAddRideClicked, stringResource(id = R.string.add_ride)) })

        LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d8))) {
            item {
                BikeChart(modifier = Modifier, ridesViewModel)
            }
            items(rideList.size) {
                when (val ride = rideList[it]) {
                    is DisplayRideItem.Divider -> {
                        Text(text = ride.text, color = Gray, modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.d8)))
                    }

                    is DisplayRideItem.RideItemData -> {
                        RideItem(
                            ride, ridesViewModel.currentDistanceUnit, BackgroundColor, listOf(
                                ThreeDotsDropdownItem("Edit", R.drawable.icon_edit, White, {onEditRideClick(ride.rideId)}, White, Typography.titleSmall),
                                ThreeDotsDropdownItem("Delete", R.drawable.icon_delete, White, { ridesViewModel.deleteRide(ride) }, White, Typography.titleSmall)
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d12)))
            }
        }
    }
}


@Composable
fun RideItem(rideEntity: DisplayRideItem.RideItemData, currentDistanceUnit: DistanceUnit, backgroundColor: Color, threeDotsItems: List<ThreeDotsDropdownItem>) {
    val duration = rideEntity.durationInMinutes.toTime()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.d4)))
            .background(backgroundColor)
            .padding(horizontal = dimensionResource(id = R.dimen.d8), vertical = dimensionResource(id = R.dimen.d4)), horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = rideEntity.rideTitle ?: "", color = White, style = Typography.titleMedium)

            CustomThreeDotsDropdown(
                elements = threeDotsItems
            )
        }
        ItemCharacteristics(title = stringResource(id = R.string.bike) + stringResource(id = R.string.duration_separator), value = rideEntity.bikeName)
        ItemCharacteristics(
            title = stringResource(id = R.string.distance) + stringResource(id = R.string.duration_separator),
            value = rideEntity.distanceInKm.toString() + currentDistanceUnit.name.lowercase()
        )
        ItemCharacteristics(
            title = stringResource(id = R.string.duration) + stringResource(id = R.string.duration_separator),
            value = duration.hour.toString() + HOUR_SUFFIX + duration.minute.toString() + MINUTES_SUFFIX
        )
        ItemCharacteristics(title = stringResource(id = R.string.date) + stringResource(id = R.string.duration_separator), value = rideEntity.date)
    }
}

@Composable
fun BikeChart(modifier: Modifier, ridesViewModel: RidesViewModel) {

    val scope = rememberCoroutineScope()
    val roadDistance = ridesViewModel.getDistanceForType(BikeType.ROADBIKE, scope).collectAsState()
    val mtb = ridesViewModel.getDistanceForType(BikeType.MTB, scope).collectAsState()
    val city = ridesViewModel.getDistanceForType(BikeType.HYBRID, scope).collectAsState()
    val ebike = ridesViewModel.getDistanceForType(BikeType.ELECTRIC, scope).collectAsState()

    val totalKm = ridesViewModel.getTotalDistance(scope).collectAsState()

    Box(
        modifier = modifier
            .height(320.dp)
            .background(
                BackgroundColor,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.d4))
            )
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.icon_stats), contentDescription = null, tint = White, modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d4)))
                Text(
                    text = stringResource(id = R.string.all_rides_statistics),
                    style = Typography.titleMedium,
                    color = White,
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.d8), horizontal = dimensionResource(id = R.dimen.d4))
                )
            }

            BarChart(
                barChartData = BarChartData(
                    bars = listOf(
                        BarChartData.Bar(
                            label = roadDistance.value.toString(),
                            value = roadDistance.value.toFloat(),
                            color = BikeRed
                        ),
                        BarChartData.Bar(
                            label = mtb.value.toString(),
                            value = mtb.value.toFloat(),
                            color = BikeYellow
                        ),
                        BarChartData.Bar(
                            label = city.value.toString(),
                            value = city.value.toFloat(),
                            color = BikeLime
                        ),
                        BarChartData.Bar(
                            label = ebike.value.toString(),
                            value = ebike.value.toFloat(),
                            color = Color.White
                        )
                    )
                ),
                // Optional properties.
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.d8))
                    .weight(1f, false),
                animation = simpleChartAnimation(),
                barDrawer = SimpleBarDrawer(),
                xAxisDrawer = SimpleXAxisDrawer(),
                yAxisDrawer = SimpleYAxisDrawer(labelTextSize = 1.sp),
                labelDrawer = SimpleValueDrawer(drawLocation = SimpleValueDrawer.DrawLocation.Inside)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.d48),
                        end = dimensionResource(id = R.dimen.d12)
                    )
            ) {
                BikeLabel(string = stringResource(id = R.string.road))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.d24)))
                BikeLabel(string = stringResource(id = R.string.mtb))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.d24)))
                BikeLabel(string = stringResource(id = R.string.city))
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.d24)))
                BikeLabel(string = stringResource(id = R.string.ebike))
            }
            Text(
                text = stringResource(id = R.string.total_km, totalKm.value),
                color = White, modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = dimensionResource(
                            id = R.dimen.d8
                        )
                    ),
                style = Typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BikeLabel(string: String) {
    Text(
        text = string,
        color = Gray,
        style = Typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(62.dp)
    )
}


@Preview
@Composable
fun PreviewEmptyRidesScreen() {
    EmptyRidesScreen {
        TODO()
    }
}