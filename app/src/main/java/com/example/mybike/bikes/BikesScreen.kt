package com.example.mybike.bikes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybike.CustomButton
import com.example.mybike.CustomThreeDotsDropdown
import com.example.mybike.CustomTopBar
import com.example.mybike.R
import com.example.mybike.localdatasource.roomdb.bike.BikeEntity
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.vo.Bike
import com.example.mybike.vo.ThreeDotsDropdownItem
import com.example.mybike.vo.WheelSize
import com.example.mybike.vo.toBike
import com.example.mybike.vo.toWheelSize


@Composable
fun BikesScreen(onAddBikeClicked: () -> Unit, bikeViewModel: BikesViewModel) {
    LaunchedEffect(key1 = Unit) {
        bikeViewModel.getBikes()
    }

    val bikesList = bikeViewModel.bikesList.collectAsState()

    if (bikesList.value.isEmpty()) {
        EmptyBikeScreen {
            onAddBikeClicked()
        }
    } else {
        ContentBikeScreen(onAddBikeClicked, bikesList.value, bikeViewModel)
    }
}

@Composable
fun EmptyBikeScreen(onAddBikeClicked: () -> Unit) {
    Column(
        Modifier
            .background(Black)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.bikes_screen))
        Image(painter = painterResource(id = R.drawable.missing_bike_card), contentDescription = null, modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d12)))
        Box(modifier = Modifier.fillMaxWidth()) {
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
        }
        CustomButton(
            text = stringResource(id = R.string.add_bike), enabled = true, onClick = onAddBikeClicked, modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.d12))
        )
    }
}

@Composable
fun ContentBikeScreen(onAddBikeClicked: () -> Unit, bikeList: List<BikeEntity>, bikesViewModel: BikesViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Black),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopBar(title = stringResource(id = R.string.bikes_screen), actions = { TopBarAddAction(onAddBikeClicked) })
        LazyColumn(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d8))) {
            items(bikeList.size) {
                val bike = bikeList[it]
                BikeItem(bike = bike.bikeType.toBike(), color = Color(bike.bikeColor), withSmallWheel = bike.inchWheelSize.toWheelSize() != WheelSize.BIG, bike, bikesViewModel = bikesViewModel)
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d8)))
            }
        }
    }
}

@Composable
fun BikeItem(bike: Bike, color: Color, withSmallWheel: Boolean, bikeEntity: BikeEntity, bikesViewModel: BikesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.d4)))
            .paint(painter = painterResource(id = R.drawable.layered_waves_dark_blue), contentScale = ContentScale.FillBounds)
            .padding(horizontal = dimensionResource(id = R.dimen.d8), vertical = dimensionResource(id = R.dimen.d4)),
        horizontalAlignment = Alignment.Start
    ) {
        CustomThreeDotsDropdown(
            modifier = Modifier.align(Alignment.End),
            elements = listOf(
                ThreeDotsDropdownItem("Edit", R.drawable.icon_edit, White, {}, White, Typography.titleSmall),
                ThreeDotsDropdownItem("Delete", R.drawable.icon_delete, White, {}, White, Typography.titleSmall)
            )
        )
        CustomBike(
            bike = bike, color = color, withSmallWheel = withSmallWheel, modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Text(text = bikeEntity.bikeName, color = White, style = Typography.titleMedium)
        BikeCharacteristics(title = stringResource(id = R.string.wheels), value = bikeEntity.inchWheelSize)
        BikeCharacteristics(title = stringResource(id = R.string.service_in_characteristic), value = bikeEntity.distanceServiceDueInKm.toString() + bikesViewModel.currentDistanceUnit.name.lowercase())
        
    }
}

@Composable
fun BikeCharacteristics(title: String, value: String) {
    Row {
        Text(text = title, color = White, style = Typography.headlineMedium, modifier = Modifier.padding(end = dimensionResource(id = R.dimen.d4)))
        Text(text = value, color = White, style = Typography.titleMedium)
    }
}

@Composable
fun TopBarAddAction(onAddBikeClicked: () -> Unit) {
    Row(modifier = Modifier.clickable { onAddBikeClicked() }) {
        Icon(imageVector = Icons.Default.Add, tint = Gray, contentDescription = null)
        Text(text = stringResource(id = R.string.add_bike))
    }
}


@Preview
@Composable
fun PreviewNoContentScreen() {
    EmptyBikeScreen(onAddBikeClicked = {})
}