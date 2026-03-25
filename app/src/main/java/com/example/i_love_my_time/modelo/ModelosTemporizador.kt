package com.example.i_love_my_time.modelo

// Define los colores y tipos de bloque según tu diseño
enum class TipoActividad {
    TRABAJO, DESCANSO_CORTO, DESCANSO_LARGO
}


// Representa un bloque individual (ej. "Trabajo Enfoque" - 25 min)
data class ActividadTemporizador(
    val id: String,
    val nombre: String,
    val duracionMinutos: Int,
    val tipo: TipoActividad
)

// Representa la rutina completa que agrupa las actividades
data class Rutina(
    val id: String,
    val nombre: String,
    val tiempoTotalMinutos: Int,
    val cantidadSeries: Int,
    val actividades: List<ActividadTemporizador>
)