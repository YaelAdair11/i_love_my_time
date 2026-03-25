package com.example.i_love_my_time.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.i_love_my_time.ui.navegacion.ItemNavegacion
import com.example.i_love_my_time.ui.pantallas.PantallaConfiguracion
import com.example.i_love_my_time.ui.pantallas.PantallaCrearRutina
import com.example.i_love_my_time.ui.pantallas.PantallaInicio
import com.example.i_love_my_time.ui.pantallas.PantallaRutinas

@Composable
fun AplicacionILoveMyTime() {
    val controladorNavegacion = rememberNavController()
    val elementosNavegacion = listOf(
        ItemNavegacion.Inicio,
        ItemNavegacion.Rutinas,
        ItemNavegacion.Crear,
        ItemNavegacion.Configuracion
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val entradaPilaNavegacion by controladorNavegacion.currentBackStackEntryAsState()
                val destinoActual = entradaPilaNavegacion?.destination
                elementosNavegacion.forEach { elemento ->
                    NavigationBarItem(
                        icon = { Icon(elemento.icono, contentDescription = elemento.titulo) },
                        label = { Text(elemento.titulo) },
                        selected = destinoActual?.hierarchy?.any { it.route == elemento.ruta } == true,
                        onClick = {
                            controladorNavegacion.navigate(elemento.ruta) {
                                popUpTo(controladorNavegacion.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingInterno ->
        NavHost(
            navController = controladorNavegacion,
            startDestination = ItemNavegacion.Inicio.ruta,
            modifier = Modifier.padding(paddingInterno)
        ) {
            composable(ItemNavegacion.Inicio.ruta) { PantallaInicio() }
            composable(ItemNavegacion.Rutinas.ruta) { PantallaRutinas() }
            composable(ItemNavegacion.Crear.ruta) { PantallaCrearRutina() }
            composable(ItemNavegacion.Configuracion.ruta) { PantallaConfiguracion() }
        }
    }
}