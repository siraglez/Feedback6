package com.example.feedback6.actividades

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.DatabaseProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val novelaDao by lazy { DatabaseProvider.getDatabase(this).novelaDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        // Cargar y mostrar las novelas con ubicaciones
        GlobalScope.launch(Dispatchers.IO) {
            val novelas = novelaDao.obtenerNovelas()
            withContext(Dispatchers.Main) {
                novelas.forEach { novela ->
                    if (novela.latitud != null && novela.longitud != null) {
                        val location = LatLng(novela.latitud, novela.longitud)
                        mMap.addMarker(MarkerOptions().position(location).title(novela.titulo))
                    }
                }
                // Centrar el mapa en un punto arbitrario (ej. la primera novela con ubicación)
                novelas.firstOrNull { it.latitud != null && it.longitud != null }?.let {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitud!!, it.longitud!!), 10f))
                }
            }
        }
    }
}
