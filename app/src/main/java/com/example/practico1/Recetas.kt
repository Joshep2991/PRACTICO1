package com.example.practico1

data class Receta(
    val id: Int,
    val nombre: String,
    val ingredientes: List<Ingrediente>
)