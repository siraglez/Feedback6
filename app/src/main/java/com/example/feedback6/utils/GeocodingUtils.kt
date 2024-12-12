package com.example.feedback6.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.io.IOException

object GeocodingUtils {
    fun obtenerCoordenadasDesdeDireccion(context: Context, direccion: String?): Address? {
        if (direccion.isNullOrBlank()) {
            Log.e("GeocodingUtils", "Dirección vacía o inválida.")
            return null
        }

        return try {
            val geocoder = Geocoder(context)
            val resultados = geocoder.getFromLocationName(direccion, 1)
            if (resultados.isNullOrEmpty()) {
                Log.e("GeocodingUtils", "No se encontraron resultados para la dirección: $direccion")
                null
            } else {
                resultados[0]
            }
        } catch (e: IOException) {
            Log.e("GeocodingUtils", "Error al realizar la geocodificación: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("GeocodingUtils", "Excepción inesperada: ${e.message}", e)
            null
        }
    }
}
