package com.example.mybike

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mybike.presentation.MainScreen
import com.example.mybike.presentation.SplashScreen
import com.example.mybike.presentation.bikes.AddBikeScreen
import com.example.mybike.presentation.bikes.BikeDetailsScreen
import com.example.mybike.presentation.bikes.BikeDetailsViewModel
import com.example.mybike.presentation.bikes.BikesViewModel
import com.example.mybike.presentation.rides.AddRideScreen
import com.example.mybike.presentation.rides.AddRideViewModel
import com.example.mybike.presentation.rides.RidesViewModel
import com.example.mybike.presentation.settings.SettingsViewModel
import com.example.mybike.ui.theme.MyBikeTheme
import com.example.mybike.vo.Screens
import dagger.hilt.android.AndroidEntryPoint

private const val BIKE_ID = "bikeId"
private const val RIDE_ID = "rideId"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val bikesViewModel by viewModels<BikesViewModel>()
    private val ridesViewModel by viewModels<RidesViewModel>()
    private val addRidesViewModel by viewModels<AddRideViewModel>()
    private val bikeDetailsViewModel by viewModels<BikeDetailsViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBikeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavGraph()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AppNavGraph(
        navController: NavHostController = rememberNavController(),
        startDestination: String = Screens.SPLASH_SCREEN.name
    ) {
        SideEffect {
        }
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(Screens.SPLASH_SCREEN.name) {
                SplashScreen { navController.navigate(Screens.MAIN_SCREEN.name) }
            }
            composable(Screens.MAIN_SCREEN.name) {
                MainScreen(
                    { navController.navigate(Screens.ADD_BIKE.name) },
                    { navController.navigate(Screens.ADD_RIDE.name) },
                    settingsViewModel,
                    bikesViewModel,
                    ridesViewModel,
                    { bikeId -> navController.navigate(Screens.BIKE_DETAILS_SCREEN.name + "/$bikeId") },
                    { rideId -> navController.navigate(Screens.ADD_RIDE.name + "/$rideId") }
                )
            }
            composable(Screens.ADD_BIKE.name) {
                AddBikeScreen(bikesViewModel = bikesViewModel, { navController.popBackStack() }, { navController.popBackStack() })
            }
            composable(Screens.ADD_RIDE.name + "/{$RIDE_ID}", arguments = listOf(navArgument(RIDE_ID) { type = NavType.LongType })) {
                val rideId = it.arguments?.getLong(RIDE_ID)
                AddRideScreen(rideId, ridesViewModel = addRidesViewModel, { navController.popBackStack() }, { navController.popBackStack() })

            }
            composable(Screens.BIKE_DETAILS_SCREEN.name + "/{$BIKE_ID}", arguments = listOf(navArgument(BIKE_ID) { type = NavType.LongType })) {
                val bikeId = it.arguments?.getLong(BIKE_ID)
                bikeId?.let {
                    BikeDetailsScreen(bikeId = bikeId, bikeDetailsViewModel = bikeDetailsViewModel, onBackButtonClicked = { navController.popBackStack() })
                }
            }
        }
    }
}