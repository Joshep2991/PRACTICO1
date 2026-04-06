package com.example.practico1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PantallaListaRecetas(
    modelo: RecetasViewModel = viewModel(),
    alNavegarACrear: () -> Unit
) {
    val todasRecetas by modelo.recetas.collectAsState()
    val textoBusqueda by modelo.textoBusqueda.collectAsState()
    val filtroIngrediente by modelo.filtroIngrediente.collectAsState()

    val recetasFiltradas = remember(textoBusqueda, filtroIngrediente, todasRecetas) {
        todasRecetas.filter { receta ->
            val coincideNombre = textoBusqueda.isEmpty() ||
                    receta.nombre.lowercase().contains(textoBusqueda.lowercase())
            val coincideIngrediente = filtroIngrediente.isEmpty() ||
                    receta.ingredientes.any { it.nombre.lowercase().contains(filtroIngrediente.lowercase()) }
            coincideNombre && coincideIngrediente
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { modelo.setTextoBusqueda(it) },
            label = { Text("Buscar por nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = filtroIngrediente,
            onValueChange = { modelo.setFiltroIngrediente(it) },
            label = { Text("Filtrar por ingrediente") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = alNavegarACrear, modifier = Modifier.align(Alignment.End)) {
            Text("+ Agregar receta")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(recetasFiltradas) { receta ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = receta.nombre, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ingredientes:")
                        for (ing in receta.ingredientes) {
                            Text("  - ${ing.nombre}: ${ing.cantidad}")
                        }
                    }
                }
            }
        }
    }
}