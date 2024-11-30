package com.example.feedback6.baseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.feedback6.dataClasses.Resena

class ResenaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "resenas.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_RESENAS = "resenas"
        private const val COLUMN_TITULO = "tituloNovela"
        private const val COLUMN_RESENA = "resena"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_RESENAS (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_TITULO TEXT, " +
                    "$COLUMN_RESENA TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESENAS")
        onCreate(db)
    }

    fun agregarResena(resena: Resena): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO, resena.tituloNovela)
            put(COLUMN_RESENA, resena.resena)
        }
        val result = db.insert(TABLE_RESENAS, null, values)
        db.close()
        return result != -1L
    }

    fun obtenerResenasPorTitulo(tituloNovela: String): List<Resena> {
        val resenas = mutableListOf<Resena>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_RESENAS,
            arrayOf(COLUMN_TITULO, COLUMN_RESENA),
            "$COLUMN_TITULO = ?",
            arrayOf(tituloNovela),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val resena = Resena(
                    tituloNovela = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                    resena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESENA))
                )
                resenas.add(resena)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return resenas
    }

    fun eliminarResena(tituloNovela: String, resenaTexto: String): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete(
            TABLE_RESENAS,
            "$COLUMN_TITULO = ? AND $COLUMN_RESENA = ?",
            arrayOf(tituloNovela, resenaTexto)
        )
        db.close()
        return rowsDeleted > 0
    }
}
