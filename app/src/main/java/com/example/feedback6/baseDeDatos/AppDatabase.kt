package com.example.feedback6.baseDeDatos

import androidx.room.*
import com.example.feedback6.dao.*
import com.example.feedback6.dataClasses.*

@Database(
    entities = [Usuario::class, Novela::class, Resena::class],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun novelaDao(): NovelaDao
    abstract fun resenaDao(): ResenaDao
}