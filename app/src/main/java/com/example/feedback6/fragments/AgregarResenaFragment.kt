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

class AgregarResenaFragment : Fragment() {

    private lateinit var tituloNovela: String
    private lateinit var novelaDbHelper: NovelaDatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE)
        aplicarTema()

        arguments?.let {
            tituloNovela = it.getString("tituloNovela", "")
        }
        novelaDbHelper = NovelaDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_resena, container, false)

        val etResena = view.findViewById<EditText>(R.id.etResena)
        val btnAgregarResena = view.findViewById<Button>(R.id.btnAgregarResena)

        btnAgregarResena.setOnClickListener {
            val resenaTexto = etResena.text.toString()
            if (resenaTexto.isNotBlank()) {
                if (novelaDbHelper.agregarResena(tituloNovela, resenaTexto)) {
                    Toast.makeText(requireContext(), "Reseña agregada exitosamente", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Error al agregar reseña", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "La reseña no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        requireContext().setTheme(if (temaOscuro) R.style.Theme_Feedback6_Night else R.style.Theme_Feedback6_Day)
    }

    companion object {
        fun newInstance(tituloNovela: String): AgregarResenaFragment {
            val fragment = AgregarResenaFragment()
            val args = Bundle()
            args.putString("tituloNovela", tituloNovela)
            fragment.arguments = args
            return fragment
        }
    }
}
