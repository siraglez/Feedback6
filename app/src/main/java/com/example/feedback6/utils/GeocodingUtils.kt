package com.example.feedback6.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log
import org.osmdroid.util.GeoPoint
import java.io.IOException

object GeocodingUtils {
    fun obtenerCoordenadasDesdeDireccion(context: Context, direccion: String): GeoPoint? {
        if (direccion.isBlank()) {
            Log.e("GeocodingUtils", "Dirección vacía o inválida.")
            return null
        }
        return try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocationName(direccion, 1)
            if (addresses.isNullOrEmpty()) {
                Log.e("GeocodingUtils", "No se encontraron resultados para la dirección: $direccion")
                null
            } else {
                val location = addresses[0]
                GeoPoint(location.latitude, location.longitude)
            }
        } catch (e: IOException) {
            Log.e("GeocodingUtils", "Error al obtener coordenadas para la dirección: $direccion", e)
            null
        } catch (e: IllegalArgumentException) {
            Log.e("GeocodingUtils", "Dirección inválida: $direccion", e)
            null
        }
    }
}
