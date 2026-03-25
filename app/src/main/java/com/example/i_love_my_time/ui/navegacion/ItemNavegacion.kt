package com.example.i_love_my_time.ui.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ItemNavegacion(val ruta: String, val icono: ImageVector, val titulo: String) {
    object Inicio : ItemNavegacion("inicio", Icons.Default.Home, "Inicio")
    object Rutinas : ItemNavegacion("rutinas", Icons.Default.List, "Rutinas")
    object Crear : ItemNavegacion("crear", Icons.Default.Add, "Crear")
    object Configuracion : ItemNavegacion("configuracion", Icons.Default.Settings, "Ajustes")
}