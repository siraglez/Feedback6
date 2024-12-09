package com.example.feedback6.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import org.osmdroid.util.GeoPoint
import java.util.Locale

object GeocodingUtils {

    fun obtenerCoordenadasDesdeDireccion(context: Context, direccion: String): GeoPoint? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val direcciones: List<Address>? = geocoder.getFromLocationName(direccion, 1)
        return if (!direcciones.isNullOrEmpty()) {
            GeoPoint(direcciones[0].latitude, direcciones[0].longitude)
        } else null
    }

    fun obtenerDireccionDesdeCoordenadas(context: Context, latitud: Double, longitud: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val direcciones: List<Address>? = geocoder.getFromLocation(latitud, longitud, 1)
        return if (!direcciones.isNullOrEmpty()) {
            direcciones[0].getAddressLine(0)
        } else null
    }
}
