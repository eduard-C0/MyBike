package com.example.mybike.vo

import com.example.mybike.R

sealed class BottomNavItem(val nameResourceId: Int, val iconResourceId: Int, val route: Screens) {

    object Bikes : BottomNavItem(
        nameResourceId = R.string.bikes_screen,
        iconResourceId = R.drawable.icon_bikes_inactive,
        route = Screens.BIKES
    )

    object Rides : BottomNavItem(
        nameResourceId = R.string.rides_screen,
        iconResourceId = R.drawable.rides_inactive,
        route = Screens.RIDES
    )

    object Settings : BottomNavItem(
        nameResourceId = R.string.settings_screen,
        iconResourceId = R.drawable.settings_inactive,
        route = Screens.SETTINGS
    )
}
