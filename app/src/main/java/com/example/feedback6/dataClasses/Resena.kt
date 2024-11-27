package com.example.feedback6.dataClasses

import androidx.room.*

@Entity(tableName = "resenas")
data class Resena(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tituloNovela: String,
    val resena: String
)