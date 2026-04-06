package com.example.practico1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaListaCompras(modelo: RecetasViewModel) {
    val lista by modelo.listaCompras.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lista de compras", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        if (lista.isEmpty()) {
            Text("No hay ingredientes necesarios. Asigna recetas en el plan semanal.")
        } else {
            LazyColumn {
                items(lista) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.comprado,
                            onCheckedChange = { modelo.toggleComprado(item) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${item.nombreIngrediente}: ${item.cantidad}", modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}