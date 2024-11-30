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
import com.example.feedback6.dataClasses.Novela
import com.example.feedback6.utils.GeocodingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarNovelaFragment : Fragment() {
    private val novelaDao by lazy { DatabaseProvider.getDatabase(requireContext()).novelaDao() }

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
            val titulo = etTitulo.text.toString()
            val autor = etAutor.text.toString()
            val anio = etAnio.text.toString().toIntOrNull()
            val sinopsis = etSinopsis.text.toString()
            val ubicacion = etUbicacion.text.toString()

            if (titulo.isNotBlank() && autor.isNotBlank() && anio != null && sinopsis.isNotBlank() && ubicacion.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val coordenadas = GeocodingUtils.obtenerCoordenadasDesdeDireccion(requireContext(), ubicacion)
                    if (coordenadas != null) {
                        val nuevaNovela = Novela(
                            titulo = titulo,
                            autor = autor,
                            anioPublicacion = anio,
                            sinopsis = sinopsis,
                            ubicacion = ubicacion
                        )
                        novelaDao.agregarNovela(nuevaNovela)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Novela agregada exitosamente", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Lugar no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
