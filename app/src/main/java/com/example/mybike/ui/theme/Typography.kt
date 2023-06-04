package com.example.mybike.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mybike.R

private val medium = Font(R.font.roboto_medium, FontWeight.W500)
private val regular = Font(R.font.roboto_regular, FontWeight.W300)

private val fontFamily = FontFamily(listOf(medium, regular))


val Typography = Typography(
    titleMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.W500, fontSize = 20.sp),
    titleSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.W500, fontSize = 13.sp),
    displayMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.W300, fontSize = 15.sp),
    displayLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.W300, fontSize = 17.sp),
    displaySmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.W300, fontSize = 12.sp)
)