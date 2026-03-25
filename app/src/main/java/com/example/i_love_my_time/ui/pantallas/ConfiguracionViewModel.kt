package com.example.i_love_my_time.ui.pantallas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfiguracionViewModel : ViewModel() {

    // --- TIPO DE ALARMA ---
    var sonidoCambioActividad by mutableStateOf("Beep corto")
    var sonidoSerieCompletada by mutableStateOf("Campana")
    var sonidoRutinaTerminada by mutableStateOf("Fanfarria")

    // --- MODO RECORDATORIO ---
    var recordatorioConstante by mutableStateOf(false) // false = Una sola vez, true = Constante
    var repetirCadaSegundos by mutableStateOf(10)

    // --- SEGUNDO PLANO ---
    var ejecutarEnBackground by mutableStateOf(true)

    // Funciones para actualizar los valores
    fun cambiarSonidoCambioActividad(nuevoSonido: String) { sonidoCambioActividad = nuevoSonido }
    fun cambiarSonidoSerieCompletada(nuevoSonido: String) { sonidoSerieCompletada = nuevoSonido }
    fun cambiarSonidoRutinaTerminada(nuevoSonido: String) { sonidoRutinaTerminada = nuevoSonido }

    fun toggleRecordatorioConstante(activo: Boolean) { recordatorioConstante = activo }
    fun toggleEjecutarEnBackground(activo: Boolean) { ejecutarEnBackground = activo }
}