package com.sysarcomp.clock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Public


sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    //object Home : Screen("home", "Home", Icons.Default.Home)
    object Stopwatch : Screen("stopwatch", "Stopwatch", Icons.Filled.AvTimer)
    object WorldClock : Screen("worldclock", "WorldClock", Icons.Filled.Public)
    object Alarm : Screen("alarm", "Alarm", Icons.Filled.Alarm)
}