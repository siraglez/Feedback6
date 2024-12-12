package com.example.feedback6.baseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.feedback6.dataClasses.Usuario

class UsuarioDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "usuario.db"
        private const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT, temaOscuro INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS usuarios")
            onCreate(db)
        }
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS usuarios")
            onCreate(db)
        }
    }

    //Métodos para agregar un nuevo usuario, verificar el login y obtener el tema de un usuario
    fun AgregarUsuario(usuario: Usuario) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", usuario.email)
            put("password", usuario.password)
            put("temaOscuro", if (usuario.temaOscuro) 1 else 0)
        }
        db.insert("usuarios", null, values)
        db.close()
    }

    fun verificarUsuario(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ? AND password = ?", arrayOf(email, password))

        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    fun obtenerUsuarios(): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)

        if (cursor.moveToFirst()) {
            do {
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val temaOscuro = cursor.getInt(cursor.getColumnIndexOrThrow("temaOscuro")) == 1
                usuarios.add(Usuario(email, password, temaOscuro))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return usuarios
    }

    fun agregarUsuarioSiNoExiste(usuario: Usuario) {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios WHERE email = ?", arrayOf(usuario.email))

        if (!cursor.moveToFirst()) {  // Si no existe el usuario
            val values = ContentValues().apply {
                put("email", usuario.email)
                put("password", usuario.password)
                put("temaOscuro", if (usuario.temaOscuro) 1 else 0)
            }
            db.insert("usuarios", null, values)
        }

        cursor.close()
        db.close()
    }

    fun obtenerTemaUsuario(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT temaOscuro FROM usuarios WHERE email = ?", arrayOf(email))

        var temaOscuro = false
        if (cursor.moveToFirst()) {
            temaOscuro = cursor.getInt(cursor.getColumnIndexOrThrow("temaOscuro")) == 1
        }
        cursor.close()
        db.close()
        return temaOscuro
    }
}
