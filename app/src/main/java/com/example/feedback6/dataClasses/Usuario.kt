package com.example.feedback6.dataClasses

data class Usuario(
    val email: String,
    val password: String,
    var temaOscuro: Boolean = false
)