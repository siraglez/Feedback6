package com.example.feedback6.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper

class MapaActivity : AppCompatActivity() {

    private lateinit var novelaDbHelper: NovelaDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        novelaDbHelper = NovelaDatabaseHelper(this)

        // Mostrar la imagen del mapa
        val mapa = findViewById<ImageView>(R.id.ivMapa)
        mapa.setImageResource(R.drawable.mapa_estatico)

        // Obtener novelas con ubicación
        val novelasConUbicacion = try {
            novelaDbHelper.obtenerNovelasConUbicacion()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList<Pair<String, String>>() // Retorna lista vacía en caso de error
        }

        // Mostrar las ubicaciones en el TextView
        val tvNovelasUbicaciones = findViewById<TextView>(R.id.tvNovelasUbicaciones)
        if (novelasConUbicacion.isNotEmpty()) {
            val ubicacionesTexto = novelasConUbicacion.joinToString("\n") {
                "• ${it.first} - ${it.second}"
            }
            tvNovelasUbicaciones.text = ubicacionesTexto
        } else {
            tvNovelasUbicaciones.text = "No hay novelas con ubicación definida."
        }

        // Configurar botón para volver
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Volver a la actividad anterior
        }
    }
}
