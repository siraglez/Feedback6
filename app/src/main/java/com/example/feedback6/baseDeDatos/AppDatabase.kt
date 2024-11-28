package com.example.feedback6.baseDeDatos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.feedback6.dao.NovelaDao
import com.example.feedback6.dao.ResenaDao
import com.example.feedback6.dao.UsuarioDao
import com.example.feedback6.dataClasses.*

@Database(
    entities = [Usuario::class, Novela::class, Resena::class],
    version = 2,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun novelaDao(): NovelaDao
    abstract fun resenaDao(): ResenaDao
}