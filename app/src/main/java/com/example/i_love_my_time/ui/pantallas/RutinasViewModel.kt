package com.example.i_love_my_time.ui.pantallas

import androidx.lifecycle.ViewModel
import com.example.i_love_my_time.data.RepositorioGlobal
import com.example.i_love_my_time.modelo.Rutina

class RutinasViewModel : ViewModel() {
    // Lee directamente de la memoria central
    val rutinasGuardadas = RepositorioGlobal.rutinas

    fun eliminarRutina(rutina: Rutina) {
        rutinasGuardadas.remove(rutina)
    }

    // Al darle iniciar, manda la rutina al temporizador
    fun seleccionarParaIniciar(rutina: Rutina) {
        RepositorioGlobal.rutinaActiva.value = rutina
    }
}