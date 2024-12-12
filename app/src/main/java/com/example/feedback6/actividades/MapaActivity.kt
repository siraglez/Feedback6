package com.example.feedback6.actividades

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.squareup.picasso.Picasso

class MapaActivity : AppCompatActivity() {

    private lateinit var novelaDbHelper: NovelaDatabaseHelper
    private lateinit var imageViewMapa: ImageView
    private lateinit var spinnerNovelas: Spinner
    private lateinit var editTextUbicacion: EditText
    private lateinit var btnBuscarUbicacion: Button
    private lateinit var btnVolverLista: Button
    private lateinit var urlMapaBase: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Inicializa la URL base desde strings.xml
        urlMapaBase = getString(R.string.mapa_estatico)

        novelaDbHelper = NovelaDatabaseHelper(this)
        imageViewMapa = findViewById(R.id.imageViewMapa)
        spinnerNovelas = findViewById(R.id.spinnerNovelas)
        editTextUbicacion = findViewById(R.id.editTextUbicacion)
        btnBuscarUbicacion = findViewById(R.id.btnBuscarUbicacion)
        btnVolverLista = findViewById(R.id.btnVolverLista)

        configurarSpinner()
        configurarBotonBuscar()
        configurarBotonVolver()
    }

    private fun configurarSpinner() {
        val novelas = novelaDbHelper.obtenerNovelas()
        val titulosNovelas = novelas.map { it.titulo }

        if (titulosNovelas.isEmpty()) {
            Toast.makeText(this, "No hay novelas guardadas", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titulosNovelas)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerNovelas.adapter = adapter

            spinnerNovelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val tituloSeleccionado = titulosNovelas[position]
                    val novela = novelas.find { it.titulo == tituloSeleccionado }
                    novela?.let {
                        mostrarMapa(it.ubicacion)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No hacer nada
                }
            }
        }
    }

    private fun configurarBotonBuscar() {
        btnBuscarUbicacion.setOnClickListener {
            val ubicacion = editTextUbicacion.text.toString()
            if (ubicacion.isNotBlank()) {
                mostrarMapa(ubicacion)
            } else {
                Toast.makeText(this, "Introduce una ubicación válida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarBotonVolver() {
        btnVolverLista.setOnClickListener {
            finish() // Finaliza la actividad y regresa a la anterior
        }
    }

    private fun mostrarMapa(ubicacion: String) {
        // Muestra siempre el mapa base.
        Picasso.get()
            .load(urlMapaBase)
            .placeholder(R.drawable.mapa_placeholder)
            .error(R.drawable.mapa_error)
            .into(imageViewMapa)
    }
}
