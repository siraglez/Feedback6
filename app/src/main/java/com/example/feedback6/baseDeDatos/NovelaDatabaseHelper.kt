package com.example.feedback6.baseDeDatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.feedback6.dataClasses.Novela

class NovelaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "novelas.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NOVELAS = "novelas"
        private const val COLUMN_TITULO = "titulo"
        private const val COLUMN_AUTOR = "autor"
        private const val COLUMN_ANIO_PUBLICACION = "anioPublicacion"
        private const val COLUMN_SINOPSIS = "sinopsis"
        private const val COLUMN_ES_FAVORITA = "esFavorita"
        private const val COLUMN_UBICACION = "ubicacion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NOVELAS (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_TITULO TEXT UNIQUE, " +
                    "$COLUMN_AUTOR TEXT, " +
                    "$COLUMN_ANIO_PUBLICACION INTEGER, " +
                    "$COLUMN_SINOPSIS TEXT, " +
                    "$COLUMN_ES_FAVORITA INTEGER, " +
                    "$COLUMN_UBICACION TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOVELAS")
        onCreate(db)
    }

    fun agregarNovela(novela: Novela): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO, novela.titulo)
            put(COLUMN_AUTOR, novela.autor)
            put(COLUMN_ANIO_PUBLICACION, novela.anioPublicacion)
            put(COLUMN_SINOPSIS, novela.sinopsis)
            put(COLUMN_ES_FAVORITA, if (novela.esFavorita) 1 else 0)
            put(COLUMN_UBICACION, novela.ubicacion)
        }
        val result = db.insert(TABLE_NOVELAS, null, values)
        db.close()
        return result != -1L
    }

    fun eliminarNovela(titulo: String): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete(TABLE_NOVELAS, "$COLUMN_TITULO = ?", arrayOf(titulo))
        db.close()
        return rowsDeleted > 0
    }

    fun obtenerNovelas(): List<Novela> {
        val novelas = mutableListOf<Novela>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NOVELAS", null)

        if (cursor.moveToFirst()) {
            do {
                val novela = Novela(
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                    autor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTOR)),
                    anioPublicacion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANIO_PUBLICACION)),
                    sinopsis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SINOPSIS)),
                    esFavorita = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ES_FAVORITA)) == 1,
                    ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICACION))
                )
                novelas.add(novela)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return novelas
    }

    fun actualizarFavorito(titulo: String, esFavorita: Boolean): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ES_FAVORITA, if (esFavorita) 1 else 0)
        }
        val rowsUpdated = db.update(TABLE_NOVELAS, values, "$COLUMN_TITULO = ?", arrayOf(titulo))
        db.close()
        return rowsUpdated > 0
    }

    fun obtenerNovelasFavoritas(): List<Novela> {
        val novelasFavoritas = mutableListOf<Novela>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NOVELAS WHERE $COLUMN_ES_FAVORITA = 1", null)

        if (cursor.moveToFirst()) {
            do {
                val novela = Novela(
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                    autor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTOR)),
                    anioPublicacion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ANIO_PUBLICACION)),
                    sinopsis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SINOPSIS)),
                    esFavorita = true,
                    ubicacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICACION))
                )
                novelasFavoritas.add(novela)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return novelasFavoritas
    }
}
