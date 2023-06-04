package com.example.mybike

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybike.ui.theme.MyBikeTheme
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onStartupCompleted: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1000)
        onStartupCompleted()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(painterResource(id = R.drawable.image_source_unsplash), contentScale = ContentScale.FillHeight, alignment = Alignment.CenterEnd), contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(id = R.drawable.logo), contentDescription = null, tint = Color.White)
    }
}

@Preview
@Composable
fun PreviewWelcomeScree() {
    MyBikeTheme {
        SplashScreen({})
    }
}