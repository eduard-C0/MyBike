package com.example.mybike.rides

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybike.CustomButton
import com.example.mybike.CustomTopBar
import com.example.mybike.R
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.LightBlue

@Composable
fun RidesScreen(onAddRideClicked: () -> Unit) {
    EmptyRidesScreen { onAddRideClicked() }
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
            text = stringResource(id = R.string.add_ride), enabled = true, onClick = onAddRideClicked, modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.d12))
        )
    }
}

@Preview
@Composable
fun PreviewEmptyRidesScreen() {
    EmptyRidesScreen {
        TODO()
    }
}