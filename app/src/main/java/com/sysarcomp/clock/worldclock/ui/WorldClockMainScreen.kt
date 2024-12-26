package com.sysarcomp.clock.worldclock.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sysarcomp.clock.worldclock.data.loadDatetime
import com.sysarcomp.clock.worldclock.data.saveDatetime
import com.sysarcomp.clock.worldclock.ui.viewmodel.WorldClockViewmodel
import kotlinx.coroutines.delay


// TODO , CAMBIAR A MAPS , PARA TENER MEJOR ESTRUCTURADA LA DATA , ej: (TIMEZONE ,DATETIME)

@Composable
fun WorldClockMainScreen() {

    val context = LocalContext.current

    var showMenu by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<String>() }

    val worldClockViewmodel: WorldClockViewmodel = viewModel()
    val viewstate by worldClockViewmodel.worldClockState

    val timezone: List<String> = viewstate.timezone
    val datetimeResponse: List<String> = viewstate.datetime

    // Mantén la lista de tiempos formateados como estado
    val datetime = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        val loadedItems = loadDatetime(context)
        selectedItems.clear()
        selectedItems.addAll(loadedItems)
    }


    // Inicializa la lista al principio
    LaunchedEffect(datetimeResponse) {
        datetime.clear()
        datetime.addAll(
            datetimeResponse.map { time ->
                val fecha = java.time.OffsetDateTime.parse(time)
                fecha.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
            }
        )
    }


    // Función para actualizar los tiempos en la lista observable
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDateTime(datetime: SnapshotStateList<String>, datetimeResponse: List<String>) {

        datetime.clear()

        if (datetimeResponse.size != 0) {

            for (i in datetimeResponse.indices) {

                try {
                    val offsetDateTime = java.time.OffsetDateTime.parse(datetimeResponse[i])
                    val currentDateTime = java.time.OffsetDateTime.now()
                        .withOffsetSameInstant(offsetDateTime.offset)
                    val formattedTime = currentDateTime.format(
                        java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                    )

                    val data = timezone[i] + " " + formattedTime

                    datetime.add(formattedTime)

                    if (selectedItems[i].isNotEmpty()) {
                        selectedItems[i] = data
                    }

                } catch (e: Exception) {
                    //datetime.add("Error") // Manejo de errores si el tiempo es inválido
                }
            }
        }
    }


    // Inicializa y actualiza periódicamente
    LaunchedEffect(datetimeResponse) {

        updateDateTime(datetime, datetimeResponse)

        while (true) {
            delay(1 * 60 * 1000L) // Espera 3 minutos
            updateDateTime(datetime, datetimeResponse)
        }
    }


    fun addItemToSelected(data: String) {
        if (!selectedItems.contains(data)) {
            selectedItems.add(data)
            saveDatetime(context, selectedItems)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Botón flotante en la esquina inferior izquierda
        FloatingActionButton(
            onClick = { showMenu = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(56.dp), // Tamaño estándar para FAB
            containerColor = Color.DarkGray, // Fondo gris oscuro
            contentColor = Color.White      // Color del ícono
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar opción") // Ícono "+"
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Menú desplegable
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                when {
                    viewstate.loading -> {
                        DropdownMenuItem(
                            text = { Text("Cargando...") },
                            onClick = { /* No hacer nada, solo un indicador */ }
                        )
                    }

                    timezone.isNotEmpty() -> {
                        for (i in timezone.indices) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = timezone[i],
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                },
                                onClick = {
                                    if (timezone.size != 0 && datetime.size != 0) {

                                        val data = timezone[i] + " " + datetime[i]

                                        if (!selectedItems.contains(data)) {
                                            addItemToSelected(data)
                                        }
                                    }

                                    showMenu = false
                                }
                            )
                        }
                    }

                    else -> {
                        DropdownMenuItem(
                            text = { Text("Sin datos disponibles") },
                            onClick = { /* No hacer nada */ }
                        )
                    }
                }
            }

            selectedItems.forEach { item ->

                Text(
                    text = item, fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }


        }

    }


}





