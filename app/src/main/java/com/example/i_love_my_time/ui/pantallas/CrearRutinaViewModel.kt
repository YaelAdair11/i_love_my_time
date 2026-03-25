package com.example.i_love_my_time.ui.pantallas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.i_love_my_time.data.RepositorioGlobal
import com.example.i_love_my_time.modelo.ActividadTemporizador
import com.example.i_love_my_time.modelo.Rutina
import com.example.i_love_my_time.modelo.TipoActividad
import java.util.UUID

class CrearRutinaViewModel : ViewModel() {
    var nombreRutina by mutableStateOf("")
    var repeticiones by mutableStateOf(1)
    val actividades = mutableStateListOf<ActividadTemporizador>()

    fun actualizarNombre(nuevoNombre: String) { nombreRutina = nuevoNombre }
    fun disminuirRepeticiones() { if (repeticiones > 1) repeticiones-- }
    fun aumentarRepeticiones() { repeticiones++ }
    fun eliminarActividad(actividad: ActividadTemporizador) { actividades.remove(actividad) }

    fun agregarActividad(nombre: String, duracion: Int, tipo: TipoActividad) {
        actividades.add(
            ActividadTemporizador(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                duracionMinutos = duracion,
                tipo = tipo
            )
        )
    }

    // AHORA SÍ GUARDA LA RUTINA REAL
    fun guardarRutina(alGuardar: () -> Unit) {
        if (nombreRutina.isNotBlank() && actividades.isNotEmpty()) {
            val tiempoTotal = actividades.sumOf { it.duracionMinutos } * repeticiones
            val nuevaRutina = Rutina(
                id = UUID.randomUUID().toString(),
                nombre = nombreRutina,
                tiempoTotalMinutos = tiempoTotal,
                cantidadSeries = repeticiones,
                actividades = actividades.toList()
            )
            RepositorioGlobal.rutinas.add(nuevaRutina)

            // Limpiar formulario
            nombreRutina = ""
            actividades.clear()
            repeticiones = 1

            // Avisar a la pantalla que ya terminó
            alGuardar()
        }
    }
}