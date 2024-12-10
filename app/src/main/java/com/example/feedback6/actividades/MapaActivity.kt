package com.example.feedback6.actividades

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.dataClasses.Novela
import com.example.feedback6.utils.GeocodingUtils
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapaActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var spinnerNovelas: Spinner
    private val novelaDbHelper by lazy { NovelaDatabaseHelper(this) }
    private lateinit var novelas: List<Novela>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_mapa)

        // Configuración del mapa
        mapView = findViewById(R.id.map)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)
        mapController.setCenter(GeoPoint(0.0, 0.0)) // Centro inicial

        spinnerNovelas = findViewById(R.id.spinnerNovelas)
        cargarNovelasEnSpinner()
        cargarNovelasEnMapa()

        findViewById<Button>(R.id.btnMiUbicacion).setOnClickListener {
            verificarPermisosUbicacion()
        }

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarNovelasEnSpinner() {
        // Obtener las novelas de la base de datos
        novelas = novelaDbHelper.obtenerNovelas()

        // Crear un adaptador para el Spinner
        val titulosNovelas = novelas.map { it.titulo }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titulosNovelas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNovelas.adapter = adapter

        // Configurar listener para el Spinner
        spinnerNovelas.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val novelaSeleccionada = novelas[position]
                moverMapaANovela(novelaSeleccionada)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // No hacer nada si no hay selección
            }
        })
    }

    private fun cargarNovelasEnMapa() {
        GlobalScope.launch(Dispatchers.IO) {
            novelas.forEach { novela ->
                val coordenadas = GeocodingUtils.obtenerCoordenadasDesdeDireccion(this@MapaActivity, novela.ubicacion)
                if (coordenadas != null) {
                    withContext(Dispatchers.Main) {
                        val geoPoint = GeoPoint(coordenadas.latitude, coordenadas.longitude)
                        val marker = Marker(mapView)
                        marker.position = geoPoint
                        marker.title = novela.titulo
                        marker.snippet = "Autor: ${novela.autor}\nUbicación: ${novela.ubicacion}"
                        mapView.overlays.add(marker)
                    }
                }
            }
        }
    }

    private fun moverMapaANovela(novela: Novela) {
        GlobalScope.launch(Dispatchers.IO) {
            val coordenadas = GeocodingUtils.obtenerCoordenadasDesdeDireccion(this@MapaActivity, novela.ubicacion)
            if (coordenadas != null) {
                withContext(Dispatchers.Main) {
                    val geoPoint = GeoPoint(coordenadas.latitude, coordenadas.longitude)
                    mapView.controller.animateTo(geoPoint)

                    // Añadir un marcador para la novela seleccionada
                    val marker = Marker(mapView)
                    marker.position = geoPoint
                    marker.title = novela.titulo
                    marker.snippet = "Autor: ${novela.autor}\nUbicación: ${novela.ubicacion}"
                    mapView.overlays.add(marker)

                    Toast.makeText(this@MapaActivity, "Moviendo a la novela: ${novela.titulo}", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MapaActivity, "Ubicación no válida para la novela: ${novela.titulo}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verificarPermisosUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            mostrarUbicacionActual()
        }
    }

    private fun mostrarUbicacionActual() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            val geoPoint = GeoPoint(lastKnownLocation.latitude, lastKnownLocation.longitude)
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.title = "Mi ubicación actual"
            mapView.overlays.add(marker)
            mapView.controller.animateTo(geoPoint)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDetach()
    }
}
