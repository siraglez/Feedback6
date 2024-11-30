package com.example.feedback6.actividades

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var novelaDbHelper: NovelaDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        novelaDbHelper = NovelaDatabaseHelper(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val novelas = novelaDbHelper.obtenerNovelas()
        val geocoder = Geocoder(this)

        novelas.forEach { novela ->
            val location = geocoder.getFromLocationName(novela.ubicacion, 1)
            if (location!!.isNotEmpty()) {
                val latLng = LatLng(location[0].latitude, location[0].longitude)
                map.addMarker(MarkerOptions().position(latLng).title(novela.titulo))
            }
        }
    }
}
