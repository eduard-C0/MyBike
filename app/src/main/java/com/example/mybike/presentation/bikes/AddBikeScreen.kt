package com.example.mybike.presentation.bikes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.mybike.presentation.CustomButton
import com.example.mybike.presentation.CustomDropDown
import com.example.mybike.presentation.CustomLabel
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.R
import com.example.mybike.ui.theme.BikeAqua
import com.example.mybike.ui.theme.BikeBlue
import com.example.mybike.ui.theme.BikeBrown
import com.example.mybike.ui.theme.BikeGray
import com.example.mybike.ui.theme.BikeGreen
import com.example.mybike.ui.theme.BikeLime
import com.example.mybike.ui.theme.BikeMarine
import com.example.mybike.ui.theme.BikePink
import com.example.mybike.ui.theme.BikeRed
import com.example.mybike.ui.theme.BikeTeal
import com.example.mybike.ui.theme.BikeYellow
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.Blue
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.toWheelSize
import com.example.mybike.vo.BikeToShow
import com.example.mybike.vo.WheelSize
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBikeScreen(bikesViewModel: BikesViewModel, onAddBikeClicked: () -> Unit, onExitButtonClicked: () -> Unit) {
    var bikeNameInputText by remember { mutableStateOf(TextFieldValue("")) }
    var serviceInInputText by remember { mutableStateOf(TextFieldValue("")) }
    var wheelSize by remember { mutableStateOf(WheelSize.BIG) }
    var defaultBikeSwitch by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {

            CustomTopBar(
                title = stringResource(id = R.string.add_bike),
                actions = {
                    Icon(painter = painterResource(id = R.drawable.icon_x), contentDescription = null, tint = White, modifier = Modifier
                        .clickable { onExitButtonClicked() }
                        .padding(
                            end = dimensionResource(
                                id = R.dimen.d12
                            )
                        ))
                }
            )

            CircularList(modifier = Modifier
                .background(Black)
                .padding(top = dimensionResource(id = R.dimen.d8), bottom = dimensionResource(id = R.dimen.d12)),
                onColorChanged = { color -> bikesViewModel.saveCurrentColor(color) })
            HorizontalPagerWithDots(bikesViewModel = bikesViewModel)
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d12))) {

                CustomLabel(stringResource(id = R.string.bike_name), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)

                OutlinedTextField(
                    value = bikeNameInputText,
                    onValueChange = {
                        bikeNameInputText = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedContainerColor = DarkBlue,
                        unfocusedContainerColor = DarkBlue,
                        disabledContainerColor = DarkBlue,
                        focusedBorderColor = Gray,
                    ),
                    textStyle = Typography.displayMedium,
                    maxLines = 1,
                    isError = bikeNameInputText.text.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (bikeNameInputText.text.isBlank()) RequiredText()

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d4)))
                CustomLabel(stringResource(id = R.string.wheel_size), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)

                CustomDropDown(
                    elements = listOf(WheelSize.BIG.value, WheelSize.SMALL.value),
                    onSelectedItem = { value ->
                        bikesViewModel.saveWheelSize(value)
                        wheelSize = value.toWheelSize()
                    },
                    selectedItem = wheelSize.value,
                    modifier = Modifier
                        .padding(bottom = dimensionResource(id = R.dimen.d12))
                )

                CustomLabel(stringResource(id = R.string.service_in), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
                OutlinedTextField(
                    value = serviceInInputText,
                    onValueChange = {
                        serviceInInputText = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        disabledTextColor = White,
                        focusedContainerColor = DarkBlue,
                        unfocusedContainerColor = DarkBlue,
                        disabledContainerColor = DarkBlue,
                        focusedBorderColor = Gray,
                        disabledBorderColor = Gray,
                        unfocusedBorderColor = Gray
                    ),
                    maxLines = 1,
                    textStyle = Typography.displayMedium,
                    isError = serviceInInputText.text.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { Text(text = bikesViewModel.currentDistanceUnit.name) }
                )
                if (serviceInInputText.text.isBlank()) RequiredText()

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d12)))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.default_bike), color = Gray, style = Typography.displayMedium)
                    Switch(
                        checked = defaultBikeSwitch,
                        onCheckedChange = { defaultBikeSwitch = !defaultBikeSwitch },
                        colors = SwitchDefaults.colors(checkedTrackColor = Blue),
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.d8))
                    )
                }
            }
        }
        CustomButton(
            text = stringResource(id = R.string.add_bike),
            enabled = serviceInInputText.text.isNotBlank() && bikeNameInputText.text.isNotBlank(),
            onClick = {
                onAddBikeClicked()
                bikesViewModel.addBike(defaultBikeSwitch, serviceInInputText.text, bikeNameInputText.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.d8))
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun RequiredText() {
    Column {
        Text(
            text = stringResource(id = R.string.required_field),
            color = Red,
            textAlign = TextAlign.Start,
            modifier = Modifier.align(Alignment.Start),
            style = Typography.displaySmall
        )
    }
}


@Composable
fun CustomBike(bikeToShow: BikeToShow, color: Color?, withSmallWheel: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Image(painter = painterResource(id = if (withSmallWheel) bikeToShow.smallWheel else bikeToShow.bigWheel), contentDescription = null)
        color?.let { Icon(painter = painterResource(id = bikeToShow.middle), contentDescription = null, tint = it) } ?: Icon(
            painter = painterResource(id = bikeToShow.middle),
            contentDescription = null
        )
        Image(painter = painterResource(id = bikeToShow.over), contentDescription = null)
    }
}

@Composable
fun CircularList(modifier: Modifier = Modifier, onColorChanged: (color: Color) -> Unit) {
    val list = listOf(BikeRed, BikeYellow, BikeBlue, BikeBrown, BikeGray, BikeGreen, BikeLime, BikeMarine, BikeAqua, BikePink, BikeTeal, White)
    val listState = rememberLazyListState()
    LazyRow(
        modifier = modifier,
        state = listState
    ) {
        items(Int.MAX_VALUE, itemContent = {
            val index = it % list.size

            val borderColor by remember {
                derivedStateOf {

                    val layoutInfo = listState.layoutInfo
                    val visibleItemsInfo = layoutInfo.visibleItemsInfo
                    val firstItemInfo = visibleItemsInfo.firstOrNull()
                    val lastItemInfo = visibleItemsInfo.lastOrNull()
                    firstItemInfo?.let { first ->
                        lastItemInfo?.let { last ->
                            if (it == (first.index + last.index) / 2) {
                                onColorChanged(list[index])
                                return@derivedStateOf White
                            }
                        }
                    }
                    Transparent
                }
            }
            Box(
                modifier = Modifier
                    .background(shape = CircleShape, color = list[index])
                    .size(dimensionResource(id = R.dimen.d20))
                    .border(2.dp, borderColor, shape = CircleShape),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.d20)))
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerWithDots(
    bikesViewModel: BikesViewModel
) {
    val currentColor = bikesViewModel.currentColor.collectAsState()
    val currentBike = bikesViewModel.currentSelectedBikeToShow.collectAsState()
    val currentWheelSize = bikesViewModel.currentWheelSize.collectAsState()

    val listOfBikeToShows = listOf(BikeToShow.RoadBike, BikeToShow.MtbBike, BikeToShow.ElectricBike, BikeToShow.HybridBike)

    val pageCount = listOfBikeToShows.size
    val pagerState = rememberPagerState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.paint(painter = painterResource(id = R.drawable.layered_waves_haikei), contentScale = ContentScale.Inside)
    ) {
        HorizontalPager(
            pageCount = pageCount,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            Box(
                Modifier
                    .height(200.dp)
                    .background(Transparent)
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                CustomBike(
                    bikeToShow = listOfBikeToShows[page],
                    color = currentColor.value,
                    withSmallWheel = currentWheelSize.value != WheelSize.BIG
                )
            }
        }

        bikesViewModel.saveCurrentBike(listOfBikeToShows[pagerState.currentPage])

        Text(text = currentBike.value.bikeType.textToShow, color = White)

        Row(
            Modifier
                .height(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)

                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewCustomBike() {
    CustomBike(BikeToShow.ElectricBike, BikeRed, false)
}

@Preview()
@Composable
fun PreviewCircularList() {
//    SingleItemLazyRow(items = , itemWidth = )
}

