package com.example.feedback6.actividades

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R

class MapaActivity : AppCompatActivity() {

    private lateinit var mapImage: Bitmap
    private val markers = mutableListOf<Pair<String, PointF>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Cargar la imagen del mapa
        mapImage = BitmapFactory.decodeResource(resources, R.drawable.mapa_estatico)

        // Añadir marcadores con coordenadas ficticias
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

        // Referencia al ImageView que contiene el mapa
        val ivMapa = findViewById<ImageView>(R.id.ivMapa)
        ivMapa.setOnDrawListener { drawMarkersOnCanvas(it) }

        // Evento para volver
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }
    }

    private fun addMarker(name: String, point: PointF) {
        markers.add(name to point)
    }

    private fun drawMarkersOnCanvas(canvas: Canvas) {
        val paint = Paint().apply {
            color = android.graphics.Color.RED
            style = Paint.Style.FILL
            textSize = 36f // Tamaño de la letra para el nombre del marcador
        }

        // Dibujar los marcadores
        markers.forEach { (name, point) ->
            canvas.drawCircle(point.x, point.y, 20f, paint) // Dibuja el círculo del marcador
            canvas.drawText(name, point.x + 25f, point.y, paint) // Dibuja el nombre del marcador
        }
    }
}
