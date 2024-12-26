package com.sysarcomp.clock.worldclock.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sysarcomp.clock.worldclock.api.worldclockService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorldClockViewmodel : ViewModel() {

    private val _worldClockState = mutableStateOf(WorldclockState())
    val worldClockState: State<WorldclockState> = _worldClockState

    private var hasFetchedData = false

    /*La data class RecipeState se declara para representar el estado de la interfaz de usuario en un solo objeto dentro de RecipeViewModel.
     Es un patrón común en arquitecturas como MVVM (Model-View-ViewModel) para encapsular y manejar el estado de forma organizada*/
    data class WorldclockState(

        val loading: Boolean = true,
        val timezone: List<String> = emptyList(),
        val datetime: List<String> = emptyList(),
        val error: String? = null
    )

    //El bloque init es un bloque de inicialización en Kotlin
    // que se ejecuta tan pronto como se crea la instancia de la clase.
    init {
        fetchWorldclock()
    }


    // corrutina para invocar al api service
    /*
    * Cuando fetchCategories() se llame, intentará cargar las categorías a través del servicio de API.
    * Si la solicitud falla, actualizará el estado de la UI para que muestre un mensaje de error,
    * que puede ser observado desde la UI y presentado al usuario.
    * */

    private fun fetchWorldclock() {

        if (hasFetchedData) return
        hasFetchedData = true

        /*
        * Inicia una corutina en el contexto de viewModelScope.
        * viewModelScope es un CoroutineScope específico del ViewModel que se cancela automáticamente cuando el ViewModel se destruye,
        * evitando problemas de memoria (leaks) al cancelar las tareas en segundo plano si el usuario cierra la pantalla.
        * */

        viewModelScope.launch {

            try {

                coroutineScope {

                    val responses = withContext(Dispatchers.IO) {
                        listOf(
                            worldclockService.getWorldclockResponseMadrid(),
                            worldclockService.getWorldclockResponseNewyork(),
                            worldclockService.getWorldclockResponseBuenosaires(),
                            worldclockService.getWorldclockResponseTokyo(),
                            worldclockService.getWorldclockResponseSydney()
                        )
                    }

                    val listTimezone = responses.map {
                        it.timezone.substringAfter("/").replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() }
                    }

                    val listDatetime = responses.map { it.datetime }

                    _worldClockState.value = _worldClockState.value.copy(
                        timezone = listTimezone.toList(),
                        datetime = listDatetime.toList(),
                        loading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {

                /*Si ocurre un error, este código actualiza _recipeState para reflejar el estado de error en la UI.
                Usa copy para generar una nueva instancia de RecipeState, cambiando los valores*/
                _worldClockState.value = _worldClockState.value.copy(
                    loading = false,
                    error = "Error fetching data ${e.message}"
                )
            }
        }
    }

}