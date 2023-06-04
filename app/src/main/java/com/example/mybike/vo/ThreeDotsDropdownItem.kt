package com.example.mybike.vo

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class ThreeDotsDropdownItem(
    val text: String,
    @DrawableRes val icon: Int,
    val iconTint: Color,
    val onClick: () -> Unit,
    val textColor: Color,
    val textStyle: TextStyle
)
