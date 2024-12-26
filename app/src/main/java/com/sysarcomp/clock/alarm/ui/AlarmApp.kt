package com.sysarcomp.clock.alarm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sysarcomp.clock.alarm.data.loadAlarms
import com.sysarcomp.clock.alarm.model.UserAlarm
import com.sysarcomp.clock.alarm.logic.setAlarm
import com.sysarcomp.clock.alarm.ui.components.TimePickerList
import com.sysarcomp.clock.alarm.ui.components.UserAlarmList

@Composable
fun AlarmApp() {

    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    val context = LocalContext.current

    // es una variable de estado reactiva, cuando se actuliza la lista , se fuerza una recomposicion en la UI
    val userAlarms = remember { mutableStateListOf<UserAlarm>() }

    var requestCode by remember { mutableStateOf(0) }


    // Cargar la lista de alarmas al iniciar la pantalla
    LaunchedEffect(Unit) { // Ejecuta solo una vez al inicio
        val loadedAlarms = loadAlarms(context)
        userAlarms.clear() // Limpia la lista actual por si hay datos previos
        userAlarms.addAll(loadedAlarms) // Añade las alarmas cargadas sin perder la reactividad
    }


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Alarma") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Configurar Alarma", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(40.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                TimePickerList(
                    label = "Hora",
                    range = 0..23,
                    selectedValue = hour,
                    onValueChange = { hour = it }
                )

                Spacer(modifier = Modifier.width(16.dp))

                TimePickerList(
                    label = "Minuto",
                    range = 0..59,
                    selectedValue = minute,
                    onValueChange = { minute = it }
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            Button(onClick = {
                setAlarm(context, hour, minute, userAlarms, requestCode++)
            }) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.height(44.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),

                ) {
                UserAlarmList(userAlarms)
            }

        }


    }
}












