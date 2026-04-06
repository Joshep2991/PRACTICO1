package com.example.practico1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCrearReceta(
    modelo: RecetasViewModel,
    alGuardar: () -> Unit
) {
    var nombreReceta by remember { mutableStateOf("") }
    var listaIngredientes by remember { mutableStateOf(mutableListOf<Ingrediente>()) }

    fun agregarIngrediente() {
        listaIngredientes = listaIngredientes.toMutableList().apply { add(Ingrediente("", "")) }
    }

    fun eliminarIngrediente(indice: Int) {
        listaIngredientes = listaIngredientes.toMutableList().apply { removeAt(indice) }
    }

    fun actualizarIngrediente(indice: Int, nombre: String, cantidad: String) {
        val nuevaLista = listaIngredientes.toMutableList()
        nuevaLista[indice] = Ingrediente(nombre, cantidad)
        listaIngredientes = nuevaLista
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = nombreReceta,
            onValueChange = { nombreReceta = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ingredientes:", style = MaterialTheme.typography.titleSmall)

        listaIngredientes.forEachIndexed { indice, ing ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = ing.nombre,
                    onValueChange = { actualizarIngrediente(indice, it, ing.cantidad) },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = ing.cantidad,
                    onValueChange = { actualizarIngrediente(indice, ing.nombre, it) },
                    label = { Text("Cantidad") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { eliminarIngrediente(indice) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }

        Button(onClick = { agregarIngrediente() }) {
            Text("+ Agregar ingrediente")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (nombreReceta.isNotBlank() && listaIngredientes.isNotEmpty() &&
                    listaIngredientes.all { it.nombre.isNotBlank() }
                ) {
                    modelo.agregarReceta(nombreReceta, listaIngredientes)
                    alGuardar()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar receta")
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}