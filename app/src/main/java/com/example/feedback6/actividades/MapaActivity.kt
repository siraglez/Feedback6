package com.example.feedback6.actividades

import android.graphics.PointF
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R

class MapaActivity : AppCompatActivity() {

    private lateinit var ivMapa: ImageView
    private lateinit var btnVolver: Button
    private val markers = mutableListOf<Pair<String, PointF>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        ivMapa = findViewById(R.id.ivMapa)

        loadMarkers() // Cargar ubicaciones ficticias

        // Configurar botón de volver
        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve atrás
        }
    }

    private fun loadMarkers() {
        addMarker("Madrid", PointF(820f, 1040f))
        addMarker("Boadilla del Monte", PointF(760f, 1080f))
        addMarker("Las Rozas", PointF(740f, 950f))
        addMarker("Villanueva de la Cañada", PointF(800f, 960f))
        addMarker("Alcobendas", PointF(710f, 900f))
        addMarker("Fuenlabrada", PointF(650f, 1070f))
        addMarker("Getafe", PointF(690f, 1100f))
        addMarker("Parla", PointF(740f, 1170f))
        addMarker("Majadahonda", PointF(800f, 1040f))
    }

    private fun addMarker(name: String, point: PointF) {
        markers.add(name to point)
    }
}
