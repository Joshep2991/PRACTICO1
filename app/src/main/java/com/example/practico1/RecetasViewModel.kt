package com.example.practico1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecetasViewModel : ViewModel() {
    private val _recetas = MutableStateFlow<List<Receta>>(emptyList())
    val recetas: StateFlow<List<Receta>> = _recetas.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    private val _filtroIngrediente = MutableStateFlow("")
    val filtroIngrediente: StateFlow<String> = _filtroIngrediente.asStateFlow()

    private val _planSemanal = MutableStateFlow<Map<DiaSemana, Int?>>(
        DiaSemana.values().associateWith { null }
    )
    val planSemanal: StateFlow<Map<DiaSemana, Int?>> = _planSemanal.asStateFlow()

    private val _listaCompras = MutableStateFlow<List<ItemCompra>>(emptyList())
    val listaCompras: StateFlow<List<ItemCompra>> = _listaCompras.asStateFlow()

    private var siguienteId = 1

    init {
        agregarRecetasEjemplo()
        actualizarListaCompras()
    }

    private fun agregarRecetasEjemplo() {
        val receta1 = Receta(
            id = siguienteId++,
            nombre = "Ensalada Cesar",
            ingredientes = listOf(
                Ingrediente("Lechuga", "1 cabeza"),
                Ingrediente("Pollo", "200g"),
                Ingrediente("Salsa Cesar", "3 cucharadas")
            )
        )
        val receta2 = Receta(
            id = siguienteId++,
            nombre = "Pasta con Tomate",
            ingredientes = listOf(
                Ingrediente("Pasta", "250g"),
                Ingrediente("Tomate", "3 unidades"),
                Ingrediente("Ajo", "2 dientes")
            )
        )
        _recetas.update { listOf(receta1, receta2) }
    }

    fun agregarReceta(nombre: String, ingredientes: List<Ingrediente>) {
        val nueva = Receta(id = siguienteId++, nombre = nombre, ingredientes = ingredientes)
        _recetas.update { it + nueva }
        actualizarListaCompras()
    }

    fun setTextoBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun setFiltroIngrediente(texto: String) {
        _filtroIngrediente.value = texto
    }

    fun asignarRecetaADia(dia: DiaSemana, recetaId: Int?) {
        val nuevoPlan = planSemanal.value.toMutableMap()
        nuevoPlan[dia] = recetaId
        _planSemanal.update { nuevoPlan }
        actualizarListaCompras()
    }

    private fun actualizarListaCompras() {
        val recetasEnPlan = planSemanal.value.values.mapNotNull { recetaId ->
            recetas.value.find { it.id == recetaId }
        }.distinctBy { it.id }

        val mapaAgrupado = mutableMapOf<String, MutableList<String>>()
        for (receta in recetasEnPlan) {
            for (ing in receta.ingredientes) {
                mapaAgrupado.getOrPut(ing.nombre) { mutableListOf() }.add(ing.cantidad)
            }
        }

        val nuevaLista = mapaAgrupado.map { (nombre, cantidades) ->
            val cantidadCombinada = if (cantidades.size == 1) cantidades.first()
            else cantidades.joinToString(", ")
            ItemCompra(nombre, cantidadCombinada, comprado = false)
        }

        val listaActual = _listaCompras.value
        val itemsConEstado = nuevaLista.map { nuevoItem ->
            val existente = listaActual.find { it.nombreIngrediente == nuevoItem.nombreIngrediente }
            if (existente != null) {
                ItemCompra(nuevoItem.nombreIngrediente, nuevoItem.cantidad, existente.comprado)
            } else {
                nuevoItem
            }
        }
        _listaCompras.update { itemsConEstado }
    }

    fun toggleComprado(item: ItemCompra) {
        _listaCompras.update { lista ->
            lista.map {
                if (it.nombreIngrediente == item.nombreIngrediente) {
                    it.copy(comprado = !it.comprado)
                } else it
            }
        }
    }
}