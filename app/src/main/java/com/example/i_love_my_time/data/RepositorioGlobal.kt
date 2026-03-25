package com.example.i_love_my_time.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.i_love_my_time.modelo.Rutina

object RepositorioGlobal {
    // Aquí se guardarán las rutinas reales que el usuario cree
    val rutinas = mutableStateListOf<Rutina>()

    // Aquí guardamos la rutina que el usuario decide iniciar
    var rutinaActiva = mutableStateOf<Rutina?>(null)
}