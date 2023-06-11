package com.example.mybike.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.mybike.presentation.CustomDropDown
import com.example.mybike.presentation.CustomLabel
import com.example.mybike.presentation.CustomTopBar
import com.example.mybike.R
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.Blue
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.NONE_ITEM
import com.example.mybike.utils.SuffixTransformer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {

    LaunchedEffect(key1 = Unit) {
        settingsViewModel.getSettingsDistanceUnit()
        settingsViewModel.getServiceReminderDistance()
        settingsViewModel.getDefaultBike()
        settingsViewModel.getServiceReminderNotificationStatus()
        settingsViewModel.getBikes()
    }

    //TODO: Get the value from the view model, If empty 100KM default
    val distanceUnit = settingsViewModel.distanceUnit.collectAsState()
    val defaultBike = settingsViewModel.defaultBike.collectAsState()
    val reminderNotificationStatus = settingsViewModel.serviceReminderNotificationStatus.collectAsState()
    val serviceReminderDistance = settingsViewModel.serviceReminderDistance.collectAsState()
    val bikes = settingsViewModel.bikeList.collectAsState()

    var serviceReminderInputText by remember(serviceReminderDistance.value) { mutableStateOf(TextFieldValue(serviceReminderDistance.value.toString())) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        CustomTopBar(title = stringResource(id = R.string.settings_screen), backgroundColor = BackgroundColor)
        CustomDropDown(
            elements = listOf("KM", "MI"),
            CustomLabel = {
                CustomLabel(stringResource(id = R.string.distance_units), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            },
            selectedItem = distanceUnit.value.name,
            onSelectedItem = { value -> settingsViewModel.saveDistanceUnit(value) },
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
                .padding(bottom = dimensionResource(id = R.dimen.d12))
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
                .padding(bottom = dimensionResource(id = R.dimen.d12)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                CustomLabel(stringResource(id = R.string.service_reminder), iconId = null, textColor = Gray, iconTint = null)

                OutlinedTextField(
                    value = serviceReminderInputText,
                    onValueChange = {
                        serviceReminderInputText = it
                        settingsViewModel.saveServiceReminderDistance(it.text)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedContainerColor = DarkBlue,
                        unfocusedContainerColor = DarkBlue,
                        disabledContainerColor = DarkBlue,
                        focusedBorderColor = Gray,
                    ), textStyle = Typography.displayMedium,
                    trailingIcon = { Text(text = distanceUnit.value.name, color = Gray) }
                )
            }

            Switch(
                checked = reminderNotificationStatus.value,
                onCheckedChange = { settingsViewModel.saveServiceReminderNotificationStatus(it) },
                colors = SwitchDefaults.colors(checkedTrackColor = Blue),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.d8))
            )
        }

        CustomDropDown(
            elements = bikes.value.map { it.bikeName },
            CustomLabel = {
                CustomLabel(stringResource(id = R.string.default_bike), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            },
            selectedItem = defaultBike.value.ifBlank { NONE_ITEM },
            onSelectedItem = {},
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.d8))
                .padding(bottom = dimensionResource(id = R.dimen.d12))
        )

    }
}