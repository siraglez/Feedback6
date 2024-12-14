package com.example.feedback6.actividades

import android.graphics.PointF
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import kotlin.math.pow

class MapaActivity : AppCompatActivity() {

    private lateinit var ivMapa: ImageView
    private lateinit var btnVolver: Button
    private lateinit var btnCargarMarcadores: Button
    private val markers = mutableListOf<Pair<String, PointF>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        ivMapa = findViewById(R.id.ivMapa)
        ivMapa.setImageResource(R.drawable.mapa_estatico)

        btnCargarMarcadores = findViewById(R.id.btnCargarMarcadores)
        btnCargarMarcadores.setOnClickListener {
            loadMarkers() // Cargar ubicaciones ficticias
        }

        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve atrás
        }

        ivMapa.setOnTouchListener { _, event ->
            for ((name, point) in markers) {
                val distance = distanceTo(event.x, event.y, point.x, point.y)
                if (distance < 50) {
                    showMarkerInfo(name)
                    ivMapa.performClick()
                    break
                }
            }
            true
        }
    }

    private fun loadMarkers() {
        markers.clear() // Limpiar los marcadores anteriores
        addMarker("Madrid", PointF(820f, 1040f))
        addMarker("Boadilla del Monte", PointF(760f, 1080f))
        addMarker("Las Rozas", PointF(740f, 950f))
        addMarker("Villanueva de la Cañada", PointF(800f, 960f))
        addMarker("Alcobendas", PointF(710f, 900f))
        addMarker("Fuenlabrada", PointF(650f, 1070f))
        addMarker("Getafe", PointF(690f, 1100f))
        addMarker("Parla", PointF(740f, 1170f))
        addMarker("Majadahonda", PointF(800f, 1040f))
        addMarker("Pozuelo de Alarcón", PointF(780f, 1080f))
        addMarker("Torrejón de Ardoz", PointF(720f, 920f))
        addMarker("Sebastián de Los Reyes", PointF(710f, 940f))

        // Redraw markers
        ivMapa.invalidate()
    }

    private fun addMarker(name: String, point: PointF) {
        markers.add(name to point)
    }

    private fun distanceTo(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(((x2 - x1).toDouble().pow(2) + (y2 - y1).toDouble().pow(2))).toFloat()
    }

    private fun showMarkerInfo(name: String) {
        Toast.makeText(this, "Novela: $name", Toast.LENGTH_SHORT).show()
    }
}
