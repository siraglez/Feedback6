package com.example.feedback6.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.dataClasses.Novela
import com.example.feedback6.utils.GeocodingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarNovelaFragment : Fragment() {

    private val novelaDbHelper by lazy { NovelaDatabaseHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_novela, container, false)

        val etTitulo = view.findViewById<EditText>(R.id.etTitulo)
        val etAutor = view.findViewById<EditText>(R.id.etAutor)
        val etAnio = view.findViewById<EditText>(R.id.etAnio)
        val etSinopsis = view.findViewById<EditText>(R.id.etSinopsis)
        val etUbicacion = view.findViewById<EditText>(R.id.etUbicacion)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregar)
        val btnVolver = view.findViewById<Button>(R.id.btnVolver)

        btnAgregar.setOnClickListener {
            val ubicacion = etUbicacion.text.toString()

            if (ubicacion.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val coordenadas = GeocodingUtils.obtenerCoordenadasDesdeDireccion(requireContext(), ubicacion)
                    if (coordenadas != null) {
                        val nuevaNovela = Novela(
                            titulo = etTitulo.text.toString(),
                            autor = etAutor.text.toString(),
                            anioPublicacion = etAnio.text.toString().toInt(),
                            sinopsis = etSinopsis.text.toString(),
                            ubicacion = ubicacion
                        )
                        novelaDbHelper.agregarNovela(nuevaNovela)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Novela agregada exitosamente", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Dirección no válida", Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, "Por favor, ingresa una dirección válida (ej: Calle, Ciudad, País).", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Por favor completa la ubicación", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
