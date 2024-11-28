package com.example.feedback6.actividades

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.DatabaseProvider
import com.example.feedback6.dao.NovelaDao
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

    private lateinit var map: GoogleMap
    private lateinit var novelaDao: NovelaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        novelaDao = DatabaseProvider.getDatabase(this).novelaDao()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        GlobalScope.launch(Dispatchers.IO) {
            val novelas = novelaDao.obtenerNovelas()
            withContext(Dispatchers.Main) {
                novelas.forEach { novela ->
                    val location = obtenerCoordenadasDesdeUbicacion(novela.ubicacion)
                    if (location != null) {
                        map.addMarker(MarkerOptions().position(location).title(novela.titulo))
                    }
                }
            }
        }
    }

    private fun obtenerCoordenadasDesdeUbicacion(ubicacion: String): LatLng? {
        // Utiliza Geocoder para convertir ubicaciones en coordenadas
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(ubicacion, 1)
        return if (addresses!!.isNotEmpty()) {
            val location = addresses[0]
            LatLng(location.latitude, location.longitude)
        } else null
    }
}
