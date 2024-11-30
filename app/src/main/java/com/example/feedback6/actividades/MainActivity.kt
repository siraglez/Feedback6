package com.example.feedback6.actividades

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.fragments.ListaNovelasFragment

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("UsuarioPreferences", MODE_PRIVATE)
        aplicarTema()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListaNovelasFragment())
                .commit()
        }

        val btnConfiguracion = findViewById<Button>(R.id.btnConfiguracion)
        btnConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        setTheme(if (temaOscuro) R.style.Theme_Feedback6_Night else R.style.Theme_Feedback6_Day)
    }
}
