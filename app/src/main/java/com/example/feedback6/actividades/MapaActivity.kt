package com.example.feedback6.actividades

import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R

class MapaActivity : AppCompatActivity() {

    private lateinit var ivMapa: ImageView
    private val markers = mutableListOf<Pair<String, PointF>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        ivMapa = findViewById(R.id.ivMapa)
        ivMapa.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.mapa_estatico))

        loadMarkers() // Cargar ubicaciones ficticias
    }

    private fun loadMarkers() {
        addMarker("Madrid", PointF(820f, 1040f))
        addMarker("Boadilla del Monte", PointF(760f, 1080f))
        addMarker("Las Rozas", PointF(740f, 950f))
        addMarker("Villanueva de la Cañada", PointF(800f, 960f))
        addMarker("Alcobendas", PointF(710f, 900f))
        addMarker("Móstoles", PointF(660f, 1010f))
        addMarker("Fuenlabrada", PointF(650f, 1070f))
        addMarker("Getafe", PointF(690f, 1100f))
        addMarker("Parla", PointF(740f, 1170f))
        addMarker("Majadahonda", PointF(800f, 1040f))
        addMarker("Pozuelo de Alarcón", PointF(780f, 1080f))
        addMarker("Torrejón de Ardoz", PointF(720f, 920f))
        addMarker("Sebastián de Los Reyes", PointF(710f, 940f))
    }

    private fun addMarker(name: String, point: PointF) {
        markers.add(name to point)
    }
}
