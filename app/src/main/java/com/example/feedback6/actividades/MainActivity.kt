package com.example.feedback6.actividades

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.DatabaseProvider
import com.example.feedback6.dao.NovelaDao
import com.example.feedback6.fragments.AgregarNovelaFragment
import com.example.feedback6.fragments.DetallesNovelaFragment
import com.example.feedback6.fragments.ListaNovelasFragment
import com.example.feedback6.dataClasses.Novela
import com.example.feedback6.fragments.AgregarResenaFragment

class MainActivity : AppCompatActivity(), ListaNovelasFragment.OnNovelaSelectedListener {

    private lateinit var novelaDao: NovelaDao
    private lateinit var sharedPreferences: SharedPreferences
    private var temaOscuro: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("UsuarioPreferences", MODE_PRIVATE)
        temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        aplicarTema()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        novelaDao = DatabaseProvider.getDatabase(this).novelaDao()

        // Verificar si el Intent del widget tiene el objeto novela
        val novela = intent.getSerializableExtra("novela") as? Novela

        if (novela != null) {
            // Si hay un título de novela, mostrar el fragmento con los detalles
            mostrarDetallesNovelaFragment(novela)
        }

        // Cargar el fragmento de lista de novelas al inicio
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ListaNovelasFragment())
                .commit()
        }

        // Configuración del botón de configuración para abrir ConfiguracionActivity
        val btnConfiguracion = findViewById<Button>(R.id.btnConfiguracion)
        btnConfiguracion.setOnClickListener {
            val intent = Intent(this, ConfiguracionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNovelaSelected(novela: Novela) {
        val detallesFragment = DetallesNovelaFragment.newInstance(novela)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detallesFragment)
            .addToBackStack(null)
            .commit()
    }

    fun mostrarAgregarNovelaFragment() {
        val agregarFragment = AgregarNovelaFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, agregarFragment)
            .addToBackStack(null)
            .commit()
    }

    fun mostrarAgregarResenaFragment(novela: Novela) {
        val agregarResenaFragment = AgregarResenaFragment.newInstance(novela.titulo)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, agregarResenaFragment)
            .addToBackStack(null)
            .commit()
    }

    fun mostrarDetallesNovelaFragment(novela: Novela) {
        val fragment = DetallesNovelaFragment.newInstance(novela)

        // Iniciar una transacción para agregar el fragmento
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        setTheme(if (temaOscuro) R.style.Theme_Feedback6_Night else R.style.Theme_Feedback6_Day)
    }

    override fun onResume() {
        super.onResume()
        // Verificar si el tema ha cambiado al regresar de ConfiguracionActivity
        val nuevoTemaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        if (nuevoTemaOscuro != temaOscuro) {
            temaOscuro = nuevoTemaOscuro // Actualizar el estado del tema
            restartActivity() // Reiniciar la actividad solo si el tema cambió
        }
    }

    private fun restartActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}