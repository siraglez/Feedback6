package com.example.feedback6.utils

import android.content.Context

object GeocodingUtils {
    fun obtenerCoordenadasDesdeDireccion(context: Context, direccion: String?): android.location.Address? {
        if (direccion.isNullOrBlank()) {
            android.util.Log.e("GeocodingUtils", "Dirección vacía o inválida.")
            return null
        }

        return try {
            val geocoder = android.location.Geocoder(context)
            val resultados = geocoder.getFromLocationName(direccion, 1)
            if (resultados!!.isNotEmpty()) {
                resultados[0] // Devuelve la primera coincidencia encontrada
            } else {
                null
            }
        } catch (e: Exception) {
            android.util.Log.e("GeocodingUtils", "Error al convertir dirección: ${e.message}", e)
            null
        }
    }
}

