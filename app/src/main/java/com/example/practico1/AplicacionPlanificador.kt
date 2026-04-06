package com.example.practico1

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AplicacionPlanificador() {
    val controlNavegacion = rememberNavController()
    val modelo: RecetasViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val opciones = listOf("Recetas", "Crear", "Plan", "Compras")
                val rutas = listOf("lista", "crear", "plan", "compras")
                opciones.forEachIndexed { indice, etiqueta ->
                    NavigationBarItem(
                        selected = false,
                        onClick = { controlNavegacion.navigate(rutas[indice]) },
                        icon = {
                            when (indice) {
                                0 -> Icon(Icons.Default.List, contentDescription = "Recetas")
                                1 -> Icon(Icons.Default.Add, contentDescription = "Crear")
                                2 -> Icon(Icons.Default.CalendarToday, contentDescription = "Plan")
                                3 -> Icon(Icons.Default.ShoppingCart, contentDescription = "Compras")
                                else -> Icon(Icons.Default.List, contentDescription = "Menu")
                            }
                        },
                        label = { Text(etiqueta) }
                    )
                }
            }
        }
    ) { paddingInterno ->
        NavHost(
            navController = controlNavegacion,
            startDestination = "lista",
            modifier = Modifier.padding(paddingInterno)
        ) {
            composable("lista") {
                PantallaListaRecetas(modelo = modelo) {
                    controlNavegacion.navigate("crear")
                }
            }
            composable("crear") {
                PantallaCrearReceta(modelo = modelo) {
                    controlNavegacion.popBackStack()
                }
            }
            composable("plan") {
                PantallaPlanSemanal(modelo = modelo)
            }
            composable("compras") {
                PantallaListaCompras(modelo = modelo)
            }
        }
    }
}