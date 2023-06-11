package com.example.mybike.presentation.rides

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.mybike.R
import com.example.mybike.localdatasource.roomdb.ride.RideEntity
import com.example.mybike.presentation.CustomButton
import com.example.mybike.presentation.CustomDropDown
import com.example.mybike.presentation.CustomLabel
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.presentation.TimerPickerDialog
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.DURATION_FORMAT
import com.example.mybike.utils.HOUR_SUFFIX
import com.example.mybike.utils.MINUTES_SUFFIX
import com.example.mybike.utils.NONE_ITEM
import com.example.mybike.utils.toTime
import com.example.mybike.vo.Time
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRideScreen(rideId: Long?, ridesViewModel: AddRideViewModel, onExitButtonClicked: () -> Unit, onAddBikeClicked: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        ridesViewModel.getBikes()
        if (rideId != null) {
            ridesViewModel.getRide(rideId)
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            if (rideId != null) {
                ridesViewModel.resetRide()
            }
        }
    }
    val bikesList = ridesViewModel.bikesList.collectAsState()
    val currentRide = ridesViewModel.currentRide.collectAsState()
    val bikeName = ridesViewModel.bikeName.collectAsState()
    val name = bikeName.value

    val ride = currentRide.value ?: RideEntity(0, 0, "", 0, 0, "")
    val hour = ride.durationInMinutes.toTime().hour
    val minute = ride.durationInMinutes.toTime().minute

    var rideTitleInputText by remember(ride) { mutableStateOf(TextFieldValue(ride.rideTitle ?: "")) }
    var selectedBike by remember(name) { mutableStateOf(name ?: "") }
    var distanceInputText by remember(ride) { mutableStateOf(TextFieldValue(if (ride.distanceInKm == 0) "" else ride.distanceInKm.toString())) }
    var durationInputText by remember(ride) { mutableStateOf(TextFieldValue(hour.toString() + HOUR_SUFFIX + minute.toString() + MINUTES_SUFFIX)) }
    var dateInputText by remember(ride) { mutableStateOf(TextFieldValue(ride.date)) }
    var openDatePickerDialog by remember { mutableStateOf(false) }
    var openTimePickerDialog by remember { mutableStateOf(false) }
    var initialHour by remember(ride) { mutableStateOf(hour.toString()) }
    var initialMinute by remember(ride) { mutableStateOf(minute.toString()) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
        ) {
            CustomTopBar(
                title = if (rideId == null) stringResource(id = R.string.add_ride) else stringResource(id = R.string.edit_ride),
                actions = {
                    Icon(painter = painterResource(id = R.drawable.icon_x), contentDescription = null, tint = White, modifier = Modifier
                        .clickable { onExitButtonClicked() }
                        .padding(
                            end = dimensionResource(
                                id = R.dimen.d12
                            )
                        ))
                },
                backgroundColor = BackgroundColor
            )

            CustomLabel(stringResource(id = R.string.ride_title), iconId = null, textColor = Gray, iconTint = null)
            OutlinedTextField(
                value = rideTitleInputText,
                onValueChange = {
                    rideTitleInputText = it
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedContainerColor = DarkBlue,
                    unfocusedContainerColor = DarkBlue,
                    disabledContainerColor = DarkBlue,
                    focusedBorderColor = Gray,
                    disabledBorderColor = Gray,
                    unfocusedBorderColor = Gray
                ),
                textStyle = Typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.d12)),
            )

            CustomLabel(stringResource(id = R.string.bike), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            CustomDropDown(
                elements = bikesList.value.map { it.bikeName },
                onSelectedItem = { value -> selectedBike = value },
                selectedItem = selectedBike.ifBlank { NONE_ITEM },
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.d12))
            )

            CustomLabel(stringResource(id = R.string.distance), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            OutlinedTextField(
                value = distanceInputText,
                onValueChange = {
                    distanceInputText = it
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedContainerColor = DarkBlue,
                    unfocusedContainerColor = DarkBlue,
                    disabledContainerColor = DarkBlue,
                    focusedBorderColor = Gray,
                    disabledBorderColor = Gray,
                    unfocusedBorderColor = Gray
                ), textStyle = Typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.d12)),
                trailingIcon = { Text(text = ridesViewModel.currentDistanceUnit.name, color = Gray) }
            )

            CustomLabel(stringResource(id = R.string.duration), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            OutlinedTextField(
                value = durationInputText,
                onValueChange = {
                    durationInputText = it
                },
                enabled = false,
                readOnly = true,
                maxLines = 1,
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
                textStyle = Typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.d12))
                    .clickable { openTimePickerDialog = true },
            )

            CustomLabel(stringResource(id = R.string.date), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            OutlinedTextField(
                value = dateInputText,
                onValueChange = {
                    dateInputText = it
                },
                enabled = false,
                readOnly = true,
                maxLines = 1,
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
                textStyle = Typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.d12))
                    .clickable { openDatePickerDialog = true }
            )

            if (openDatePickerDialog) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
                val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
                val simpleDateFormat = SimpleDateFormat(DURATION_FORMAT, Locale.UK)
                DatePickerDialog(onDismissRequest = {
                    openDatePickerDialog = false
                }, confirmButton = {
                    TextButton(
                        onClick = {
                            openDatePickerDialog = false
                            dateInputText = TextFieldValue(simpleDateFormat.format(datePickerState.selectedDateMillis))
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(stringResource(id = R.string.ok))
                    }
                },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDatePickerDialog = false
                            }
                        ) {
                            Text(stringResource(id = R.string.cancel))
                        }
                    }) {
                    DatePicker(state = datePickerState)
                }
            }
        }
        if (openTimePickerDialog) {
            TimerPickerDialog(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                //remember this values outside
                initialHour = initialHour,
                initialMinute = initialMinute,
                { openTimePickerDialog = false },
                { time ->
                    durationInputText = TextFieldValue(time.hour.toString() + HOUR_SUFFIX + time.minute.toString() + MINUTES_SUFFIX)
                    initialHour = time.hour.toString()
                    initialMinute = time.minute.toString()
                    openTimePickerDialog = false
                }
            )
        }

        CustomButton(
            text = if (rideId == null) stringResource(id = R.string.add_ride) else stringResource(id = R.string.save),
            enabled = selectedBike.isNotBlank() && distanceInputText.text.isNotBlank() && durationInputText.text.isNotBlank() && dateInputText.text.isNotBlank(),
            onClick = {
                if (rideId != null) {
                    ridesViewModel.updateRide(rideId, selectedBike, rideTitleInputText.text, distanceInputText.text, dateInputText.text, initialHour, initialMinute)
                } else {
                    ridesViewModel.addRide(selectedBike, rideTitleInputText.text, distanceInputText.text, dateInputText.text, initialHour, initialMinute)
                }
                onAddBikeClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.d8))
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
                .align(Alignment.BottomCenter)
        )
    }
}

