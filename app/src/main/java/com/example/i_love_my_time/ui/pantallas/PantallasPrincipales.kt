package com.example.i_love_my_time.ui.pantallas

import android.media.RingtoneManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


// 1. PANTALLA INICIO (TEMPORIZADOR)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(viewModel: InicioViewModel = viewModel()) {
    val context = LocalContext.current

    // Carga la rutina seleccionada al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarRutinaSeleccionada()
    }

    // Escucha cuando el temporizador llega a cero para reproducir sonido
    LaunchedEffect(viewModel.dispararAlarma) {
        if (viewModel.dispararAlarma) {
            try {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone.play()
                kotlinx.coroutines.delay(3000) // Suena por 3 segundos
                ringtone.stop()
            } catch (e: Exception) { e.printStackTrace() }
            viewModel.alarmaReproducida()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Barra superior azul
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "TaskTimer",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = { /* Pendiente: Ir a ajustes */ }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF378ADD)
            )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta central gris del temporizador
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("actividad actual", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        text = viewModel.actividadActualNombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.obtenerTiempoFormateado(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 56.sp,
                        color = Color(0xFF222222)
                    )

                    LinearProgressIndicator(
                        progress = viewModel.progreso,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF378ADD),
                        trackColor = Color(0xFFDDDDDD)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de control
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.presionarStop() }) {
                            BotonCircularControl(icono = "■", tamañoBoton = 40.dp, tamañoTexto = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        if (viewModel.estaCorriendo) {
                            IconButton(onClick = { viewModel.presionarPausa() }) {
                                BotonCircularControl(icono = "❚❚", tamañoBoton = 64.dp, tamañoTexto = 24.sp, colorFondo = Color(0xFFE24B4A))
                            }
                        } else {
                            IconButton(onClick = { viewModel.presionarPlay() }) {
                                BotonCircularControl(icono = "▶", tamañoBoton = 64.dp, tamañoTexto = 24.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        BotonCircularControl(icono = "▶|", tamañoBoton = 40.dp, tamañoTexto = 14.sp)
                    }
                }
            }

            // Indicador de Series
            Text(
                text = "Serie ${viewModel.serieActual} de ${viewModel.totalSeries}",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(viewModel.totalSeries) { indice ->
                    val colorPunto = if (indice < viewModel.serieActual) Color(0xFF378ADD) else Color(0xFFDDDDDD)
                    Box(modifier = Modifier.size(10.dp).background(colorPunto, CircleShape))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Lista de próximas actividades
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Próximas actividades", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))

                viewModel.listaProximas.value.forEach { actividad ->
                    val colorHex = when(actividad.tipo) {
                        com.example.i_love_my_time.modelo.TipoActividad.TRABAJO -> 0xFF378ADD
                        com.example.i_love_my_time.modelo.TipoActividad.DESCANSO_CORTO -> 0xFFE24B4A
                        com.example.i_love_my_time.modelo.TipoActividad.DESCANSO_LARGO -> 0xFF4CAF50
                    }
                    FilaActividad(colorHex = colorHex, nombre = actividad.nombre, tiempo = "${actividad.duracionMinutos}:00")
                    Divider(color = Color(0xFFEEEEEE))
                }
            }
        }
    }
}


// 2. PANTALLA MIS RUTINAS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRutinas(
    navController: NavController,
    viewModel: RutinasViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis rutinas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("crear") },
                containerColor = Color(0xFF378ADD),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear rutina")
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(viewModel.rutinasGuardadas) { rutina ->
                TarjetaRutina(
                    rutina = rutina,
                    onIniciar = {
                        viewModel.seleccionarParaIniciar(rutina)
                        navController.navigate("inicio")
                    },
                    onEditar = { /* Pendiente */ },
                    onEliminar = { viewModel.eliminarRutina(rutina) }
                )
            }
        }
    }
}


// 3. PANTALLA CREAR RUTINA

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearRutina(
    navController: NavController,
    viewModel: CrearRutinaViewModel = viewModel()
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Crear rutina", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text("Nombre de la rutina", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = viewModel.nombreRutina,
                onValueChange = { viewModel.actualizarNombre(it) },
                placeholder = { Text("Ej. Pomodoro Estándar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Actividades", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            viewModel.actividades.forEach { actividad ->
                val colorActividad = when(actividad.tipo) {
                    com.example.i_love_my_time.modelo.TipoActividad.TRABAJO -> 0xFF378ADD
                    com.example.i_love_my_time.modelo.TipoActividad.DESCANSO_CORTO -> 0xFFE24B4A
                    com.example.i_love_my_time.modelo.TipoActividad.DESCANSO_LARGO -> 0xFF4CAF50
                }
                FilaActividad(colorHex = colorActividad, nombre = actividad.nombre, tiempo = "${actividad.duracionMinutos} min")
                Divider(color = Color(0xFFEEEEEE))
            }

            TextButton(
                onClick = { mostrarDialogo = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("+ Agregar actividad", color = Color(0xFF378ADD), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Repeticiones", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Repetir serie", color = Color(0xFF333333))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Veces", color = Color.Gray, modifier = Modifier.padding(end = 16.dp))
                    IconButton(onClick = { viewModel.disminuirRepeticiones() }) {
                        Icon(Icons.Default.Add, contentDescription = "Menos", tint = Color.Gray)
                    }
                    Text("${viewModel.repeticiones}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = { viewModel.aumentarRepeticiones() }) {
                        Icon(Icons.Default.Add, contentDescription = "Más", tint = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.guardarRutina {
                        navController.navigate("rutinas")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF378ADD)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Guardar rutina", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Nueva Actividad") },
            text = { Text("¿Qué tipo de actividad deseas agregar?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.agregarActividad("Trabajo Enfoque", 25, com.example.i_love_my_time.modelo.TipoActividad.TRABAJO)
                    mostrarDialogo = false
                }) { Text("Trabajo (25m)") }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.agregarActividad("Descanso Corto", 5, com.example.i_love_my_time.modelo.TipoActividad.DESCANSO_CORTO)
                    mostrarDialogo = false
                }) { Text("Descanso (5m)") }
            }
        )
    }
}


// 4. PANTALLA CONFIGURACIÓN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConfiguracion(
    viewModel: ConfiguracionViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("TIPO DE ALARMA", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaConfiguracionSonido("Cambio de actividad", viewModel.sonidoCambioActividad)
                    Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 12.dp))
                    FilaConfiguracionSonido("Serie completada", viewModel.sonidoSerieCompletada)
                    Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 12.dp))
                    FilaConfiguracionSonido("Rutina terminada", viewModel.sonidoRutinaTerminada)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("MODO RECORDATORIO", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaConfiguracionSwitch(
                        texto = "Constante (vs Una sola vez)",
                        activo = viewModel.recordatorioConstante,
                        alCambiar = { viewModel.toggleRecordatorioConstante(it) }
                    )

                    if (viewModel.recordatorioConstante) {
                        Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Repetir cada", color = Color(0xFF333333))
                            Text("${viewModel.repetirCadaSegundos} seg", color = Color(0xFF378ADD), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("SEGUNDO PLANO", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaConfiguracionSwitch(
                        texto = "Ejecutar en background",
                        activo = viewModel.ejecutarEnBackground,
                        alCambiar = { viewModel.toggleEjecutarEnBackground(it) }
                    )
                    Divider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(vertical = 12.dp))
                    Text(
                        text = "Configurar permisos",
                        color = Color(0xFF378ADD),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


// COMPONENTES AUXILIARES

@Composable
fun BotonCircularControl(
    icono: String,
    tamañoBoton: androidx.compose.ui.unit.Dp,
    tamañoTexto: androidx.compose.ui.unit.TextUnit,
    colorFondo: Color = Color(0xFF378ADD)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tamañoBoton)
            .background(colorFondo, CircleShape)
    ) {
        Text(icono, fontSize = tamañoTexto, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FilaActividad(colorHex: Long, nombre: String, tiempo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(10.dp).background(Color(colorHex), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(nombre, fontWeight = FontWeight.Medium, color = Color(0xFF333333), modifier = Modifier.weight(1f))
        Text(tiempo, color = Color.Gray)
    }
}

@Composable
fun TarjetaRutina(
    rutina: com.example.i_love_my_time.modelo.Rutina,
    onIniciar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = rutina.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${rutina.actividades.size} actividades • ${rutina.tiempoTotalMinutos} min totales",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onIniciar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Iniciar")
                }

                OutlinedButton(
                    onClick = onEditar,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Editar", color = Color(0xFF378ADD))
                }

                OutlinedButton(
                    onClick = onEliminar,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE24B4A))
                ) {
                    Text("Eliminar", color = Color(0xFFE24B4A))
                }
            }
        }
    }
}

@Composable
fun FilaConfiguracionSonido(etiqueta: String, valorActual: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = Color(0xFF333333))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(valorActual, color = Color.Gray, fontSize = 14.sp)
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Desplegar",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun FilaConfiguracionSwitch(texto: String, activo: Boolean, alCambiar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto, color = Color(0xFF333333))
        Switch(
            checked = activo,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF378ADD)
            )
        )
    }
}