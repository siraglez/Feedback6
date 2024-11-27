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
import com.example.feedback6.dataClasses.Resena
import com.example.feedback6.baseDeDatos.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarResenaFragment : Fragment() {
    private lateinit var tituloNovela: String
    private lateinit var sharedPreferences: SharedPreferences
    private val resenaDao by lazy { DatabaseProvider.getDatabase(requireContext()).resenaDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE)

        arguments?.let {
            tituloNovela = it.getString("tituloNovela", "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_resena, container, false)

        val etResena = view.findViewById<EditText>(R.id.etResena)
        val btnAgregarResena = view.findViewById<Button>(R.id.btnAgregarResena)

        btnAgregarResena.setOnClickListener {
            val resenaTexto = etResena.text.toString()
            if (resenaTexto.isNotBlank()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val resena = Resena(tituloNovela = tituloNovela, resena = resenaTexto)
                    resenaDao.agregarResena(resena)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Reseña agregada exitosamente", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "La reseña no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        return view
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
