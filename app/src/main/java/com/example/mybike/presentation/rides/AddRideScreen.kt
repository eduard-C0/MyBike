package com.example.mybike.presentation.rides

import android.annotation.SuppressLint
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import com.example.mybike.R
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
import com.example.mybike.vo.Time
import java.text.SimpleDateFormat
import java.util.Locale

private const val NONE_ITEM = ""

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRideScreen(ridesViewModel: AddRideViewModel, onExitButtonClicked: () -> Unit, onAddBikeClicked: () -> Unit) {
    LaunchedEffect(key1 = Unit) {
        ridesViewModel.getBikes()
    }

    val bikesList = ridesViewModel.bikesList.collectAsState()

    var rideTitleInputText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedBike by remember { mutableStateOf("") }
    var distanceInputText by remember { mutableStateOf(TextFieldValue("")) }
    var durationInputText by remember { mutableStateOf(TextFieldValue("")) }
    var dateInputText by remember { mutableStateOf(TextFieldValue("")) }
    var openDatePickerDialog by remember { mutableStateOf(false) }
    var openTimePickerDialog by remember { mutableStateOf(false) }
    var initialHour by remember { mutableStateOf("") }
    var initialMinute by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
        ) {
            CustomTopBar(
                title = stringResource(id = R.string.add_ride),
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
                selectedItem = NONE_ITEM,
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
                    ridesViewModel.saveDuration(time)
                    openTimePickerDialog = false
                }
            )
        }

        CustomButton(
            text = stringResource(id = R.string.add_ride),
            enabled = selectedBike.isNotBlank() && distanceInputText.text.isNotBlank() && durationInputText.text.isNotBlank() && dateInputText.text.isNotBlank(),
            onClick = {
                ridesViewModel.addRide(selectedBike, rideTitleInputText.text, distanceInputText.text, dateInputText.text)
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

