package com.example.feedback6.dao

import androidx.room.*
import com.example.feedback6.dataClasses.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password")
    suspend fun verificarUsuario(email: String, password: String): Usuario?

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>
}
