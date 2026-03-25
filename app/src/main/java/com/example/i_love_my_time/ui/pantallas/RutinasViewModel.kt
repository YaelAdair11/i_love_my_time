package com.example.i_love_my_time.ui.pantallas

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.i_love_my_time.modelo.Rutina

class RutinasViewModel : ViewModel() {

    // Lista de rutinas simuladas basadas en tu mockup
    val rutinasGuardadas = mutableStateListOf(
        Rutina(
            id = "1",
            nombre = "Pomodoro Clásico",
            tiempoTotalMinutos = 120,
            cantidadSeries = 4,
            actividades = emptyList() // Vacio por ahora para la prueba
        ),
        Rutina(
            id = "2",
            nombre = "HIIT 30/10",
            tiempoTotalMinutos = 20,
            cantidadSeries = 10,
            actividades = emptyList()
        ),
        Rutina(
            id = "3",
            nombre = "Estudio x bloques",
            tiempoTotalMinutos = 180,
            cantidadSeries = 3,
            actividades = emptyList()
        )
    )

    fun eliminarRutina(rutina: Rutina) {
        rutinasGuardadas.remove(rutina)
    }
}