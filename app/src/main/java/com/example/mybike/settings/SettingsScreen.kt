package com.example.mybike.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.mybike.CustomDropDown
import com.example.mybike.CustomLabel
import com.example.mybike.CustomTopBar
import com.example.mybike.R
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.Blue
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    //TODO: Get the value from the view model, If empty 100KM default
    var serviceReminderInputText by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("100")) }
    var switchOn by rememberSaveable { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        CustomTopBar(title = stringResource(id = R.string.settings_screen), backgroundColor = BackgroundColor)

        CustomDropDown(
            elements = listOf("KM", "MI"),
            CustomLabel = {
                CustomLabel(stringResource(id = R.string.distance_units), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.d12)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                CustomLabel(stringResource(id = R.string.service_reminder), iconId = null, textColor = Gray, iconTint = null)

                OutlinedTextField(
                    value = serviceReminderInputText,
                    onValueChange = { serviceReminderInputText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = White, containerColor = DarkBlue, focusedBorderColor = Gray),
                    textStyle = Typography.displayMedium,
                )
            }

            Switch(
                checked = switchOn,
                onCheckedChange = { switchOn = it },
                colors = SwitchDefaults.colors(checkedTrackColor = Blue),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.d4))
            )
        }

        CustomDropDown(
            elements = listOf(),
            CustomLabel = {
                CustomLabel(stringResource(id = R.string.default_bike), iconId = R.drawable.icon_required, textColor = Gray, iconTint = Gray)
            },
        )

    }
}