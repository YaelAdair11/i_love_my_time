package com.example.i_love_my_time.ui.pantallas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.i_love_my_time.data.RepositorioGlobal
import com.example.i_love_my_time.modelo.ActividadTemporizador
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InicioViewModel : ViewModel() {
    var actividadActualNombre by mutableStateOf("Selecciona una rutina")
        private set
    var serieActual by mutableStateOf(0)
        private set
    var totalSeries by mutableStateOf(0)
        private set
    var tiempoRestanteSegundos by mutableStateOf(0L)
        private set
    var estaCorriendo by mutableStateOf(false)
        private set
    var progreso by mutableStateOf(1.0f)
        private set
    var listaProximas = mutableStateOf<List<ActividadTemporizador>>(emptyList())
        private set

    // Control de la alarma
    var dispararAlarma by mutableStateOf(false)

    private var tiempoTotalActividad = 0L
    private var trabajoTemporizador: Job? = null
    private var indiceActividadActual = 0

    fun cargarRutinaSeleccionada() {
        val rutina = RepositorioGlobal.rutinaActiva.value
        if (rutina != null && rutina.actividades.isNotEmpty()) {
            totalSeries = rutina.cantidadSeries
            serieActual = 1
            indiceActividadActual = 0
            prepararActividad()
        } else {
            actividadActualNombre = "Sin rutina activa"
            tiempoRestanteSegundos = 0
            progreso = 0f
            listaProximas.value = emptyList()
        }
    }

    private fun prepararActividad() {
        val rutina = RepositorioGlobal.rutinaActiva.value ?: return
        val actividad = rutina.actividades[indiceActividadActual]

        actividadActualNombre = actividad.nombre
        tiempoTotalActividad = actividad.duracionMinutos * 60L
        tiempoRestanteSegundos = tiempoTotalActividad
        progreso = 1.0f

        // Actualizar lista de próximas actividades
        if (indiceActividadActual + 1 < rutina.actividades.size) {
            listaProximas.value = rutina.actividades.subList(indiceActividadActual + 1, rutina.actividades.size)
        } else {
            listaProximas.value = emptyList()
        }
    }

    fun presionarPlay() {
        if (estaCorriendo || tiempoRestanteSegundos <= 0) return
        estaCorriendo = true
        trabajoTemporizador = viewModelScope.launch {
            while (tiempoRestanteSegundos > 0) {
                delay(1000)
                tiempoRestanteSegundos--
                progreso = tiempoRestanteSegundos.toFloat() / tiempoTotalActividad.toFloat()
            }
            // Termina el tiempo de esta actividad
            estaCorriendo = false
            dispararAlarma = true
            avanzarSiguienteActividad()
        }
    }

    private fun avanzarSiguienteActividad() {
        val rutina = RepositorioGlobal.rutinaActiva.value ?: return
        indiceActividadActual++

        if (indiceActividadActual < rutina.actividades.size) {
            // Siguiente actividad en la misma serie
            prepararActividad()
            presionarPlay() // Auto-iniciar la siguiente
        } else {
            // Terminó la serie completa
            if (serieActual < totalSeries) {
                serieActual++
                indiceActividadActual = 0
                prepararActividad()
                presionarPlay()
            } else {
                // Terminó TODA la rutina
                actividadActualNombre = "¡Rutina Completada!"
                tiempoRestanteSegundos = 0
                progreso = 0f
                listaProximas.value = emptyList()
            }
        }
    }

    fun presionarPausa() {
        trabajoTemporizador?.cancel()
        estaCorriendo = false
    }

    fun presionarStop() {
        trabajoTemporizador?.cancel()
        estaCorriendo = false
        cargarRutinaSeleccionada() // Reinicia desde el principio de la serie 1
    }

    fun obtenerTiempoFormateado(): String {
        val minutos = tiempoRestanteSegundos / 60
        val segundos = tiempoRestanteSegundos % 60
        return "%02d:%02d".format(minutos, segundos)
    }

    fun alarmaReproducida() {
        dispararAlarma = false
    }
}