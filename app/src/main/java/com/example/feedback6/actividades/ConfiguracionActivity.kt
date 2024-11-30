package com.example.feedback6.actividades

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.UsuarioDatabaseHelper
import com.example.feedback6.dataClasses.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var usuarioDbHelper: UsuarioDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("UsuarioPreferences", MODE_PRIVATE)

        // Aplicar el tema al iniciar la actividad
        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        usuarioDbHelper = UsuarioDatabaseHelper(this)

        val switchTemaOscuro = findViewById<Switch>(R.id.switchTemaOscuro)
        val btnBackup = findViewById<Button>(R.id.btnBackup)
        val btnRestore = findViewById<Button>(R.id.btnRestore)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // Configurar el switch con la preferencia guardada
        val temaOscuroActivado = sharedPreferences.getBoolean("temaOscuro", false)
        switchTemaOscuro.isChecked = temaOscuroActivado

        switchTemaOscuro.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("temaOscuro", isChecked)
            editor.apply()
            aplicarTema()
            recreate()  // Recargar la actividad para aplicar el cambio de tema
        }

        // Configurar botón de copias de seguridad
        btnBackup.setOnClickListener {
            realizarCopiaDeSeguridad()
        }

        // Configurar botón de restaurar datos
        btnRestore.setOnClickListener {
            restaurarDatos()
        }

        // Configurar el botón para volver a la pantalla anterior
        btnVolver.setOnClickListener {
            finish()  // Simplemente termina la actividad para volver a MainActivity
        }

        // Configurar el botón para cerrar sesión
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        setTheme(if (temaOscuro) R.style.Theme_Feedback6_Night else R.style.Theme_Feedback6_Day)
    }

    private fun realizarCopiaDeSeguridad() {
        // Guardar un archivo de copia de seguridad con la información de los usuarios en formato JSON usando Gson
        val usuarios = usuarioDbHelper.obtenerUsuarios()
        val backupFile = File(getExternalFilesDir(null), "copia_de_seguridad_usuarios.json")

        try {
            val gson = Gson()
            val json = gson.toJson(usuarios)
            FileWriter(backupFile).use { writer ->
                writer.write(json)
            }
            Toast.makeText(this, "Copia de seguridad realizada en: ${backupFile.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error al realizar la copia de seguridad: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun restaurarDatos() {
        val backupFile = File(getExternalFilesDir(null), "copia_de_seguridad_usuarios.json")

        if (!backupFile.exists()) {
            Toast.makeText(this, "No se encontró ninguna copia de seguridad.", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val gson = Gson()
            val usuarios = FileReader(backupFile).use { reader ->
                gson.fromJson<List<Usuario>>(reader, object : TypeToken<List<Usuario>>() {}.type)
            }

            // Agregar los usuarios a la base de datos
            usuarios.forEach { usuario ->
                usuarioDbHelper.agregarUsuarioSiNoExiste(usuario)
            }

            Toast.makeText(this, "Datos restaurados exitosamente desde la copia de seguridad.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al restaurar los datos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun cerrarSesion() {
        // Borrar las preferencias del usuario para cerrar sesión
        sharedPreferences.edit().clear().apply()
        // Redirigir a la pantalla de login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
