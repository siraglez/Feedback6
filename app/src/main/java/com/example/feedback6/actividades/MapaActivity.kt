package com.example.feedback6.actividades

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper

class MapaActivity : AppCompatActivity() {

    private lateinit var novelaDbHelper: NovelaDatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        novelaDbHelper = NovelaDatabaseHelper(this)

        // Mostrar la imagen del mapa
        val mapa = findViewById<ImageView>(R.id.ivMapa)
        mapa.setImageResource(R.drawable.mapa_estatico)

        // Obtener novelas con ubicación y mostrarlas
        val novelasConUbicacion = novelaDbHelper.obtenerNovelasConUbicacion()
        novelasConUbicacion.forEach { (titulo, ubicacion) ->
        }

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Volver a la actividad anterior
        }
    }
}
