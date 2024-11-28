package com.example.feedback6.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.Locale

object GeocodingUtils {
    fun obtenerCoordenadasDesdeDireccion(context: Context, direccion: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val direcciones: List<Address> = geocoder.getFromLocationName(direccion, 1)!!
        return if (direcciones.isNotEmpty()) {
            val location = direcciones[0]
            Pair(location.latitude, location.longitude)
        } else {
            null
        }
    }

    fun obtenerDireccionDesdeCoordenadas(context: Context, latitud: Double, longitud: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val direcciones: List<Address> = geocoder.getFromLocation(latitud, longitud, 1)!!
        return if (direcciones.isNotEmpty()) {
            direcciones[0].getAddressLine(0)
        } else {
            null
        }
    }
}
