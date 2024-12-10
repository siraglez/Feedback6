package com.example.feedback6.actividades

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.utils.GeocodingUtils
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapaActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private val novelaDbHelper by lazy { NovelaDatabaseHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_mapa)

        mapView = findViewById(R.id.map)
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Usa mapas de OpenStreetMap
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        cargarNovelasEnMapa()

        findViewById<Button>(R.id.btnMiUbicacion).setOnClickListener {
            verificarPermisosUbicacion()
        }
    }

    private fun cargarNovelasEnMapa() {
        val novelas = novelaDbHelper.obtenerNovelas()
        novelas.forEach { novela ->
            val coordenadas = GeocodingUtils.obtenerCoordenadasDesdeDireccion(this, novela.ubicacion)
            if (coordenadas != null) {
                val marker = Marker(mapView)
                marker.position = coordenadas
                marker.title = novela.titulo
                marker.snippet = "Autor: ${novela.autor}"
                mapView.overlays.add(marker)
            } else {
                Log.w("MapaActivity", "No se pudieron obtener coordenadas para la novela: ${novela.titulo} con ubicación: ${novela.ubicacion}")
            }
        }
    }

    private fun verificarPermisosUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            mostrarUbicacionActual()
        }
    }

    private fun mostrarUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
            return
        }

        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocation != null) {
            actualizarMapaConUbicacion(lastKnownLocation)
        } else {
            Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show()
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_UPDATE_INTERVAL,
            LOCATION_UPDATE_DISTANCE,
            locationListener
        )
    }

    private val locationListener = LocationListener { location ->
        actualizarMapaConUbicacion(location)
    }

    private fun actualizarMapaConUbicacion(location: Location) {
        val geoPoint = GeoPoint(location.latitude, location.longitude)
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.title = "Mi ubicación actual"
        mapView.overlays.add(marker)
        mapView.controller.animateTo(geoPoint)

        Toast.makeText(this, "Ubicación actualizada: (${location.latitude}, ${location.longitude})", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mostrarUbicacionActual()
        } else {
            Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show()
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
        locationManager.removeUpdates(locationListener)
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val LOCATION_UPDATE_INTERVAL = 5000L // 5 segundos
        private const val LOCATION_UPDATE_DISTANCE = 10f   // 10 metros
    }
}
