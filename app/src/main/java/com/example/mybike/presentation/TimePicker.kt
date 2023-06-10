package com.example.mybike.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.example.mybike.R
import com.example.mybike.ui.theme.BackgroundColor
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.utils.toTime
import com.example.mybike.vo.Time
import com.example.mybike.vo.TimeType

private const val TAG = "[Presentation] : TimerPickerDialog"

@Composable
fun TimerPickerDialog(
    modifier: Modifier = Modifier,
    initialHour: String,
    initialMinute: String,
    onCancel: () -> Unit,
    onOk: (Time) -> Unit,
) {
    var selectedHour by remember(initialHour) { mutableStateOf(initialHour) }
    var selectedMinute by remember(initialMinute) { mutableStateOf(initialMinute) }

    Dialog(
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = modifier
                .background(
                    color = BackgroundColor,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.d16))
                )
                .padding(dimensionResource(id = R.dimen.d16))
                .wrapContentSize()
        ) {
            Text(text = stringResource(id = R.string.select_duration), color = Color.White)

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d12)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeCard(
                    modifier = Modifier.weight(0.5f),
                    time = selectedHour,
                    onValueChanged = {
                        selectedHour = it
                    }
                )

                Text(
                    text = stringResource(id = R.string.duration_separator),
                    style = Typography.labelMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d8))
                )

                TimeCard(
                    time = selectedMinute,
                    modifier = Modifier.weight(0.5f),
                    onValueChanged = {
                        selectedMinute = it
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.d16)))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.align(Alignment.End)
            ) {
                TextButton(
                    onClick = { onCancel() },
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = White,
                        style = Typography.labelMedium
                    )
                }
                TextButton(
                    onClick = {
                        try {
                            onOk(Time(selectedHour.toTime(TimeType.HOUR), selectedMinute.toTime(TimeType.MINUTE)))
                        } catch (exception: NumberFormatException) {
                            Log.e(TAG, exception.message.toString())
                        }
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.ok),
                        color = White,
                        style = Typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun TimeCard(
    time: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = time,
        onValueChange = { onValueChanged(it) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = White,
            unfocusedTextColor = White,
            focusedContainerColor = DarkBlue,
            unfocusedContainerColor = DarkBlue,
            disabledContainerColor = DarkBlue,
            focusedBorderColor = Gray,
        ),
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        maxLines = 1,
        placeholder = {
            Text(text = stringResource(id = R.string.zero), style = Typography.displayMedium, color = White)
        }
    )
}


