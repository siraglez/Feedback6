package com.example.feedback6.actividades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.dataClasses.Novela
import com.squareup.picasso.Picasso

class MapaActivity : AppCompatActivity() {

    private lateinit var novelaDbHelper: NovelaDatabaseHelper
    private lateinit var spinnerNovelas: Spinner
    private lateinit var mapImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        novelaDbHelper = NovelaDatabaseHelper(this)
        spinnerNovelas = findViewById(R.id.spinnerNovelas)
        mapImageView = findViewById(R.id.mapImageView)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        cargarNovelasEnSpinner()

        spinnerNovelas.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val novela = parent?.getItemAtPosition(position) as? Novela
                novela?.let {
                    actualizarMapa(it.ubicacion)
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarNovelasEnSpinner() {
        val novelas = novelaDbHelper.obtenerNovelas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, novelas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNovelas.adapter = adapter
    }

    private fun actualizarMapa(ubicacion: String) {
        val mapUrl = getString(R.string.mapa_estatico)
        Picasso.get()
            .load(mapUrl)
            .error(R.drawable.mapa_error)
            .into(mapImageView)
    }
}
