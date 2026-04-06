package com.example.practico1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPlanSemanal(modelo: RecetasViewModel) {
    val plan by modelo.planSemanal.collectAsState()
    val todasRecetas by modelo.recetas.collectAsState()
    val dias = DiaSemana.values()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(dias) { dia ->
            val idRecetaActual = plan[dia]
            val recetaActual = todasRecetas.find { it.id == idRecetaActual }
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = dia.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    var expandido by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandido,
                        onExpandedChange = { expandido = !expandido }
                    ) {
                        OutlinedTextField(
                            value = recetaActual?.nombre ?: "Ninguna",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandido,
                            onDismissRequest = { expandido = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Ninguna") },
                                onClick = {
                                    modelo.asignarRecetaADia(dia, null)
                                    expandido = false
                                }
                            )
                            todasRecetas.forEach { receta ->
                                DropdownMenuItem(
                                    text = { Text(receta.nombre) },
                                    onClick = {
                                        modelo.asignarRecetaADia(dia, receta.id)
                                        expandido = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}