package com.example.feedback6.utils

import android.content.Context
import android.location.Address
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object GeocodingUtils {

    private val client = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun obtenerCoordenadasDesdeDireccion(context: Context, direccion: String?): Address? {
        if (direccion.isNullOrBlank()) {
            android.util.Log.e("GeocodingUtils", "Dirección vacía o inválida.")
            return null
        }

        val url = "https://nominatim.openstreetmap.org/search?q=${direccion.replace(" ", "+")}&format=json&limit=1"
        val request = Request.Builder().url(url).build()

        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                android.util.Log.e("GeocodingUtils", "Error en la respuesta HTTP: ${response.message}")
                return null
            }

            val body = response.body?.string()
            if (body.isNullOrBlank()) {
                android.util.Log.e("GeocodingUtils", "Respuesta vacía.")
                return null
            }

            val json = JsonParser.parseString(body).asJsonArray
            if (json.size() > 0) {
                val firstResult = json[0].asJsonObject
                val lat = firstResult.get("lat").asDouble
                val lon = firstResult.get("lon").asDouble

                Address(context.resources.configuration.locales[0]).apply {
                    latitude = lat
                    longitude = lon
                }
            } else {
                android.util.Log.e("GeocodingUtils", "No se encontraron resultados para la dirección.")
                null
            }
        } catch (e: IOException) {
            android.util.Log.e("GeocodingUtils", "Error de red: ${e.message}", e)
            null
        } catch (e: Exception) {
            android.util.Log.e("GeocodingUtils", "Error inesperado: ${e.message}", e)
            null
        }
    }
}