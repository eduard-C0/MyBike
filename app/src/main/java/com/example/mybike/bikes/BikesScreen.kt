package com.example.mybike.bikes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybike.CustomButton
import com.example.mybike.CustomTopBar
import com.example.mybike.R
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White


@Composable
fun BikesScreen(onAddBikeClicked: () -> Unit) {
    EmptyBikeScreen {
        onAddBikeClicked()
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
            Icon(painter = painterResource(id = R.drawable.dotted_line), contentDescription = null, modifier = Modifier.padding(start = dimensionResource(id = R.dimen.d40), top = dimensionResource(id = R.dimen.d12)), tint = LightBlue)
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

@Preview
@Composable
fun PreviewNoContentScreen() {
    EmptyBikeScreen(onAddBikeClicked = {})
}