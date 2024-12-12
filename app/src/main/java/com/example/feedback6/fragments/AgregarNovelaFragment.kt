package com.example.feedback6.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.dataClasses.Novela

class AgregarNovelaFragment : Fragment() {

    private lateinit var novelaDbHelper: NovelaDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationAdapter: ArrayAdapter<String>
    private val markers = mutableListOf<Pair<String, PointF>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE)
        aplicarTema()

        novelaDbHelper = NovelaDatabaseHelper(requireContext())
        loadMarkers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_novela, container, false)

        val etTitulo = view.findViewById<EditText>(R.id.etTitulo)
        val etAutor = view.findViewById<EditText>(R.id.etAutor)
        val etAnio = view.findViewById<EditText>(R.id.etAnio)
        val etSinopsis = view.findViewById<EditText>(R.id.etSinopsis)
        val lvUbicacion = view.findViewById<ListView>(R.id.lvUbicacion)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregar)
        val btnVolver = view.findViewById<Button>(R.id.btnVolver)

        // Configuración del ListView para elegir ubicación
        locationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, markers.map { it.first })
        lvUbicacion.adapter = locationAdapter

        lvUbicacion.setOnItemClickListener { _, _, position, _ ->
            val selectedLocation = markers[position].first
            lvUbicacion.setItemChecked(position, true)
        }

        btnAgregar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val autor = etAutor.text.toString()
            val anio = etAnio.text.toString().toIntOrNull()
            val sinopsis = etSinopsis.text.toString()
            val ubicacion = markers.find { lvUbicacion.isItemChecked(markers.indexOf(it)) }?.first ?: ""

            if (titulo.isNotBlank() && autor.isNotBlank() && anio != null && sinopsis.isNotBlank() && ubicacion.isNotBlank()) {
                val novela = Novela(titulo, autor, anio, sinopsis, false, ubicacion = ubicacion)
                novelaDbHelper.agregarNovela(novela)
                Toast.makeText(requireContext(), "Novela agregada", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        requireContext().setTheme(if (temaOscuro) R.style.Theme_Feedback6_Night else R.style.Theme_Feedback6_Day)
    }

    private fun loadMarkers() {
        // Cargar ubicaciones ficticias
        addMarker("Madrid", PointF(820f, 1040f))
        addMarker("Boadilla del Monte", PointF(760f, 1080f))
        addMarker("Las Rozas", PointF(740f, 950f))
        addMarker("Villanueva de la Cañada", PointF(800f, 960f))
        addMarker("Alcobendas", PointF(710f, 900f))
        addMarker("Móstoles", PointF(660f, 1010f))
        addMarker("Fuenlabrada", PointF(650f, 1070f))
        addMarker("Getafe", PointF(690f, 1100f))
        addMarker("Parla", PointF(740f, 1170f))
        addMarker("Majadahonda", PointF(800f, 1040f))
        addMarker("Pozuelo de Alarcón", PointF(780f, 1080f))
        addMarker("Torrejón de Ardoz", PointF(720f, 920f))
        addMarker("Sebastián de Los Reyes", PointF(710f, 940f))
    }

    private fun addMarker(name: String, point: PointF) {
        markers.add(name to point)
    }
}
