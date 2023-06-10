package com.example.mybike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybike.presentation.MainScreen
import com.example.mybike.presentation.SplashScreen
import com.example.mybike.presentation.bikes.AddBikeScreen
import com.example.mybike.presentation.bikes.BikesViewModel
import com.example.mybike.presentation.rides.AddRideScreen
import com.example.mybike.presentation.rides.AddRideViewModel
import com.example.mybike.presentation.rides.RidesViewModel
import com.example.mybike.presentation.settings.SettingsViewModel
import com.example.mybike.ui.theme.MyBikeTheme
import com.example.mybike.vo.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val bikesViewModel by viewModels<BikesViewModel>()
    private val ridesViewModel by viewModels<RidesViewModel>()
    private val addRidesViewModel by viewModels<AddRideViewModel>()
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
                MainScreen({ navController.navigate(Screens.ADD_BIKE.name) }, { navController.navigate(Screens.ADD_RIDE.name) }, settingsViewModel, bikesViewModel, ridesViewModel)
            }
            composable(Screens.ADD_BIKE.name) {
                AddBikeScreen(bikesViewModel = bikesViewModel, { navController.popBackStack() }, { navController.popBackStack() })
            }
            composable(Screens.ADD_RIDE.name) {
                AddRideScreen(ridesViewModel = addRidesViewModel, { navController.popBackStack() }, { navController.popBackStack() })
            }
        }
    }
}