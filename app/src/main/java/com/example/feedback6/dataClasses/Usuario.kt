package com.example.feedback6.dataClasses

import androidx.room.*

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    var temaOscuro: Boolean = false
)