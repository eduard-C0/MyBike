package com.example.mybike.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mybike.R
import com.example.mybike.presentation.bikes.BikesScreen
import com.example.mybike.presentation.bikes.BikesViewModel
import com.example.mybike.presentation.rides.RidesScreen
import com.example.mybike.presentation.rides.RidesViewModel
import com.example.mybike.presentation.settings.SettingsScreen
import com.example.mybike.presentation.settings.SettingsViewModel
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.DefaultItem
import com.example.mybike.ui.theme.MyBikeTheme
import com.example.mybike.ui.theme.*
import com.example.mybike.ui.theme.Typography
import com.example.mybike.vo.BikeToShow
import com.example.mybike.vo.BottomNavItem
import com.example.mybike.vo.BottomNavItem.*
import com.example.mybike.vo.Screens

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    onAddBikeClicked: () -> Unit,
    onAddRideClicked: () -> Unit,
    settingsViewModel: SettingsViewModel,
    bikesViewModel: BikesViewModel,
    ridesViewModel: RidesViewModel,
    onItemClicked: (bikeId: Long) -> Unit,
    onRideEditClicked: (rideId: Long?) -> Unit
) {
    val bottomNavItems = listOf(
        Bikes,
        Rides,
        Settings
    )
    val navController = rememberNavController()

    Scaffold(bottomBar = { BottomNavigation(bottomNavItems, navController) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            NavHost(navController = navController, startDestination = Screens.BIKES.name) {
                composable(Screens.BIKES.name) {
                    BikesScreen(onAddBikeClicked, bikesViewModel, onItemClicked)
                }
                composable(Screens.RIDES.name) {
                    RidesScreen(ridesViewModel, onAddRideClicked, onRideEditClicked)
                }
                composable(Screens.SETTINGS.name) {
                    SettingsScreen(settingsViewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(items: List<BottomNavItem>, navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar(containerColor = DarkBlue, modifier = Modifier.height(dimensionResource(id = R.dimen.d60))) {
        items.forEach { item ->
            val selected = item.route.name == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { navController.navigate(item.route.name) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(painter = painterResource(id = item.iconResourceId), contentDescription = item.route.name, tint = if (selected) SelectedItem else DefaultItem)
                        Text(text = stringResource(id = item.nameResourceId), style = Typography.displaySmall, color = if (selected) SelectedItem else DefaultItem)
                    }
                },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = DarkBlue, indicatorColor = DarkBlue),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewMainScreen() {
    MyBikeTheme {
        MainScreen({}, {}, hiltViewModel(), hiltViewModel(), hiltViewModel(), {}, {})
    }
}