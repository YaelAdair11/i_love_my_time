package com.example.i_love_my_time.ui.pantallas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.i_love_my_time.modelo.ActividadTemporizador
import com.example.i_love_my_time.modelo.TipoActividad
import java.util.UUID

class CrearRutinaViewModel : ViewModel() {
    // Estados de los campos de texto
    var nombreRutina by mutableStateOf("")
    var repeticiones by mutableStateOf(1)

    // Lista dinámica que actualizará la pantalla automáticamente al agregar elementos
    val actividades = mutableStateListOf<ActividadTemporizador>()

    fun actualizarNombre(nuevoNombre: String) {
        nombreRutina = nuevoNombre
    }

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

    fun eliminarActividad(actividad: ActividadTemporizador) {
        actividades.remove(actividad)
    }

    fun disminuirRepeticiones() {
        if (repeticiones > 1) repeticiones--
    }

    fun aumentarRepeticiones() {
        repeticiones++
    }

    fun guardarRutina() {
        // En la Fase 4 conectaremos esto a la base de datos Room
        println("Rutina lista para guardar: $nombreRutina con ${actividades.size} actividades")
    }
}