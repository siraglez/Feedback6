package com.example.feedback6.fragments

import android.content.Context
import android.content.SharedPreferences
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

class AgregarNovelaFragment : Fragment() {

    private lateinit var novelaDbHelper: NovelaDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE)
        aplicarTema()

        novelaDbHelper = NovelaDatabaseHelper(requireContext())
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
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregar)
        val btnVolver = view.findViewById<Button>(R.id.btnVolver)

        btnAgregar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val autor = etAutor.text.toString()
            val anio = etAnio.text.toString().toIntOrNull()
            val sinopsis = etSinopsis.text.toString()

            if (titulo.isNotBlank() && autor.isNotBlank() && anio != null && sinopsis.isNotBlank()) {
                val novela = Novela(titulo, autor, anio, sinopsis)
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
}
