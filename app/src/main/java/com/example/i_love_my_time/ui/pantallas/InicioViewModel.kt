package com.example.i_love_my_time.ui.pantallas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InicioViewModel : ViewModel() {

    // --- DATOS FIJOS DE PRUEBA (Luego vendrán de la base de datos) ---
    private val tiempoTrabajoSegundos = 25 * 60L // 25 minutos
    val actividadActualNombre = "Trabajo Enfoque"
    val serieActual = 2
    val totalSeries = 5

    // --- ESTADOS DE LA IU (Observables por la pantalla) ---

    // Tiempo que se muestra en pantalla (en segundos)
    var tiempoRestanteSegundos by mutableStateOf(tiempoTrabajoSegundos)
        private set

    // Indica si el temporizador está corriendo o pausado
    var estaCorriendo by mutableStateOf(false)
        private set

    // Controla la barra de progreso (de 0.0 a 1.0)
    var progreso by mutableStateOf(1.0f)
        private set

    // Trabajo en segundo plano para la cuenta regresiva
    private var trabajoTemporizador: Job? = null

    // --- LÓGICA DE BOTONES ---

    fun presionarPlay() {
        if (estaCorriendo) return // Ya está corriendo

        estaCorriendo = true
        // Iniciamos un trabajo en segundo plano (Corrutina)
        trabajoTemporizador = viewModelScope.launch {
            while (tiempoRestanteSegundos > 0) {
                delay(1000) // Espera 1 segundo
                tiempoRestanteSegundos--
                // Calculamos el progreso inverso (de 1 a 0)
                progreso = tiempoRestanteSegundos.toFloat() / tiempoTrabajoSegundos.toFloat()
            }
            // Cuando llega a cero
            estaCorriendo = false
            // TODO: Aquí lanzar la alarma/sonido y pasar a descanso
        }
    }

    fun presionarPausa() {
        trabajoTemporizador?.cancel() // Detiene el trabajo en segundo plano
        estaCorriendo = false
    }

    fun presionarStop() {
        trabajoTemporizador?.cancel()
        estaCorriendo = false
        // Reiniciamos todo al estado inicial
        tiempoRestanteSegundos = tiempoTrabajoSegundos
        progreso = 1.0f
    }

    // Función auxiliar para formatear segundos a MM:SS
    fun obtenerTiempoFormateado(): String {
        val minutos = tiempoRestanteSegundos / 60
        val segundos = tiempoRestanteSegundos % 60
        return "%02d:%02d".format(minutos, segundos)
    }
}