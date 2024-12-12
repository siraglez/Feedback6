package com.example.feedback6.actividades

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.squareup.picasso.Picasso

class MapaActivity : AppCompatActivity() {

    private lateinit var spinnerNovelas: Spinner
    private lateinit var imageViewMapa: ImageView
    private lateinit var novelaDbHelper: NovelaDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Inicializar la base de datos y las vistas
        novelaDbHelper = NovelaDatabaseHelper(this)
        spinnerNovelas = findViewById(R.id.spinnerNovelas)
        imageViewMapa = findViewById(R.id.imageViewMapa)

        // Configurar el Spinner con las novelas
        configurarSpinnerNovelas()
    }

    private fun configurarSpinnerNovelas() {
        val novelas = novelaDbHelper.obtenerNovelas()
        if (novelas.isEmpty()) {
            Toast.makeText(this, "No hay novelas disponibles.", Toast.LENGTH_SHORT).show()
            return
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, novelas.map { it.titulo })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNovelas.adapter = adapter

        spinnerNovelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val tituloSeleccionado = parent.getItemAtPosition(position) as String
                val novelaSeleccionada = novelas.find { it.titulo == tituloSeleccionado }

                if (novelaSeleccionada != null) {
                    mostrarMapa(novelaSeleccionada)
                } else {
                    Toast.makeText(this@MapaActivity, "No se encontró la novela seleccionada.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada si no se selecciona ningún elemento
            }
        }
    }

    private fun mostrarMapa(novela: com.example.feedback6.dataClasses.Novela) {
        val urlMapa = getString(R.string.mapa_estatico)

        Picasso.get()
            .load(urlMapa)
            .placeholder(R.drawable.mapa_placeholder) // Imagen por defecto mientras carga
            .error(R.drawable.mapa_error) // Imagen en caso de error
            .into(imageViewMapa)
    }
}
