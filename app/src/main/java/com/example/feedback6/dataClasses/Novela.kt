package com.example.feedback6.dataClasses

import java.io.Serializable

data class Novela(
    val titulo: String,
    val autor: String,
    val anioPublicacion: Int,
    val sinopsis: String,
    var esFavorita: Boolean = false,
    val resenas: MutableList<String> = mutableListOf(),
    val ubicacion: String
) : Serializable