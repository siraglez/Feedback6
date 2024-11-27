package com.example.feedback6.actividades

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.DatabaseProvider
import com.example.feedback6.dao.UsuarioDao
import com.example.feedback6.dataClasses.Usuario
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import com.google.gson.Gson

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var usuarioDao: UsuarioDao
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("UsuarioPreferences", MODE_PRIVATE)

        //Aplicar el tema al iniciar la actividad
        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        usuarioDao = DatabaseProvider.getDatabase(this).usuarioDao()

        val switchTemaOscuro = findViewById<Switch>(R.id.switchTemaOscuro)
        val btnBackup = findViewById<Button>(R.id.btnBackup)
        val btnRestore = findViewById<Button>(R.id.btnRestore)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        //Configurar el switch con la preferencia guardada
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

        //Configurar botón de restaurar datos
        btnRestore.setOnClickListener {
            restaurarDatos()
        }

        // Configurar el botón para volver a la pantalla anterior
        btnVolver.setOnClickListener {
            finish()  // Simplemente termina la actividad para volver a MainActivity
        }

        //Configurar el botón para cerrar sesión
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        setTheme(if (temaOscuro) R.style.Theme_Feedback5_Night else R.style.Theme_Feedback5_Day)
    }

    private fun realizarCopiaDeSeguridad() {
        GlobalScope.launch(Dispatchers.IO) {
            val usuarios = usuarioDao.obtenerUsuarios()
            val backupFile = File(getExternalFilesDir(null), "usuarios_backup.json")
            try {
                val usuariosJson = Gson().toJson(usuarios)
                backupFile.writeText(usuariosJson)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConfiguracionActivity, "Copia de seguridad realizada en: ${backupFile.absolutePath}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConfiguracionActivity, "Error al realizar la copia de seguridad: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun restaurarDatos() {
        val backupFile = File(getExternalFilesDir(null), "usuarios_backup.json")
        if (!backupFile.exists()) {
            Toast.makeText(this, "No se encontró ninguna copia de seguridad.", Toast.LENGTH_LONG).show()
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val usuariosJson = backupFile.readText()
                val usuarios: List<Usuario> = Gson().fromJson(usuariosJson, object : TypeToken<List<Usuario>>() {}.type)
                usuarios.forEach { usuarioDao.agregarUsuario(it) }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConfiguracionActivity, "Datos restaurados exitosamente.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ConfiguracionActivity, "Error al restaurar los datos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun cerrarSesion() {
        // Borrar las preferecias del usuario para cerrar sesión
        sharedPreferences.edit().clear().apply()
        // Redirigir a la pantalla de login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}