package com.example.mybike

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybike.ui.theme.MyBikeTheme
import com.example.mybike.vo.Screens

class MainActivity : ComponentActivity() {
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
                MainScreen({ TODO("") }, { TODO("") })
            }
        }
    }
}