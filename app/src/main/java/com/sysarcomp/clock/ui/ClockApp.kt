package com.sysarcomp.clock.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sysarcomp.clock.alarm.ui.AlarmApp
import com.sysarcomp.clock.stopwatch.ui.StopwatchMainScreen
import com.sysarcomp.clock.ui.navigation.BottomNavigationBar
import com.sysarcomp.clock.ui.navigation.Screen
import com.sysarcomp.clock.worldclock.ui.WorldClockMainScreen

// ClockApp.kt
@Composable
fun ClockApp() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // Aquí añadimos la barra inferior
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Stopwatch.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Stopwatch.route) {
                StopwatchMainScreen()
            }

            composable(Screen.WorldClock.route) {
                WorldClockMainScreen()
            }

            composable(Screen.Alarm.route) {
                AlarmApp()
            }
        }
    }
}

@Composable
fun HomeScreen() {

}
