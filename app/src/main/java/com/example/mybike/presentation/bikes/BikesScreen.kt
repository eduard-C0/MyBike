package com.example.mybike.presentation.bikes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybike.presentation.CustomButton
import com.example.mybike.presentation.CustomProgressBar
import com.example.mybike.presentation.CustomThreeDotsDropdown
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.R
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.presentation.TopBarAddAction
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.vo.BikeToShow
import com.example.mybike.vo.ThreeDotsDropdownItem
import com.example.mybike.vo.WheelSize
import com.example.mybike.vo.toBike
import com.example.mybike.vo.toWheelSize


@Composable
fun BikesScreen(onAddBikeClicked: () -> Unit, bikeViewModel: BikesViewModel, onItemClicked: (bikeId: Long) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        bikeViewModel.getBikes()
    }

    val bikesList = bikeViewModel.bikesList.collectAsState()

    if (bikesList.value.isEmpty()) {
        EmptyBikeScreen {
            onAddBikeClicked()
        }
    } else {
        ContentBikeScreen(onAddBikeClicked, bikesList.value, bikeViewModel, onItemClicked)
    }
}

@Composable
fun EmptyBikeScreen(onAddBikeClicked: () -> Unit) {
    Column(
        Modifier
            .background(Black)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.bikes_screen))
        Image(painter = painterResource(id = R.drawable.missing_bike_card), contentDescription = null, modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d12)))
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = R.drawable.dotted_line),
                contentDescription = null,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.d40), top = dimensionResource(id = R.dimen.d12)),
                tint = LightBlue
            )
            Text(
                text = stringResource(id = R.string.no_bikes_added),
                color = White,
                modifier = Modifier
                    .background(Black)
                    .align(Alignment.Center)
                    .padding(top = dimensionResource(id = R.dimen.d12)),
                textAlign = TextAlign.Center,
                style = Typography.displayMedium
            )

            CustomButton(
                text = stringResource(id = R.string.add_bike), enabled = true, onClick = onAddBikeClicked, modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.d12))
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun ContentBikeScreen(
    onAddBikeClicked: () -> Unit, bikeList: List<BikeEntity>, bikesViewModel: BikesViewModel, onItemClicked: (bikeId: Long) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Black),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.bikes_screen), actions = { TopBarAddAction(onAddBikeClicked, stringResource(id = R.string.add_bike)) })
        LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d8))) {
            items(bikeList.size) {
                val bike = bikeList[it]
                BikeItem(
                    bikeToShow = bike.bikeType.toBike(),
                    color = Color(bike.bikeColor),
                    withSmallWheel = bike.inchWheelSize.toWheelSize() != WheelSize.BIG,
                    bike,
                    bikesViewModel = bikesViewModel,
                    onItemClicked
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d12)))
            }
        }
    }
}

@Composable
fun BikeItem(
    bikeToShow: BikeToShow,
    color: Color,
    withSmallWheel: Boolean,
    bikeEntity: BikeEntity,
    bikesViewModel: BikesViewModel,
    onItemClicked: (bikeId: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.d4)))
            .paint(painter = painterResource(id = R.drawable.layered_waves_dark_blue), contentScale = ContentScale.FillBounds)
            .padding(horizontal = dimensionResource(id = R.dimen.d8), vertical = dimensionResource(id = R.dimen.d4))
            .clickable { onItemClicked(bikeEntity.bikeId) },
        horizontalAlignment = Alignment.Start
    ) {
        CustomThreeDotsDropdown(
            modifier = Modifier.align(Alignment.End), elements = listOf(
                ThreeDotsDropdownItem("Edit", R.drawable.icon_edit, White, {}, White, Typography.titleSmall),
                ThreeDotsDropdownItem("Delete", R.drawable.icon_delete, White, { bikesViewModel.deleteBike(bikeEntity) }, White, Typography.titleSmall)
            )
        )
        CustomBike(
            bikeToShow = bikeToShow, color = color, withSmallWheel = withSmallWheel, modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(text = bikeEntity.bikeName, color = White, style = Typography.titleMedium)
        ItemCharacteristics(title = stringResource(id = R.string.wheels), value = bikeEntity.inchWheelSize)
        ItemCharacteristics(title = stringResource(id = R.string.service_in_characteristic), value = bikeEntity.distanceServiceDueInKm.toString() + bikesViewModel.currentDistanceUnit.name.lowercase())

        CustomProgressBar(bikeEntity.traveledDistanceInKm.toFloat() / bikeEntity.distanceServiceDueInKm.toFloat(), modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.d12)))
    }
}

@Composable
fun ItemCharacteristics(title: String, value: String) {
    Row {
        Text(text = title, color = Gray, style = Typography.headlineMedium, modifier = Modifier.padding(end = dimensionResource(id = R.dimen.d4)))
        Text(text = value, color = White, style = Typography.titleMedium)
    }
}


@Preview
@Composable
fun PreviewNoContentScreen() {
    EmptyBikeScreen(onAddBikeClicked = {})
}