package com.example.feedback6.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.feedback6.R
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.baseDeDatos.ResenaDatabaseHelper
import com.example.feedback6.dataClasses.Novela
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetallesNovelaFragment : Fragment() {
    private lateinit var novela: Novela
    private lateinit var resenasAdapter: ArrayAdapter<String>
    private val novelaDbHelper by lazy { NovelaDatabaseHelper(requireContext()) }
    private val resenaDbHelper by lazy { ResenaDatabaseHelper(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            novela = it.getSerializable("novela") as Novela
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detalles_novela, container, false)

        view.findViewById<TextView>(R.id.tvTitulo).text = novela.titulo
        view.findViewById<TextView>(R.id.tvAutor).text = novela.autor
        view.findViewById<TextView>(R.id.tvAnio).text = novela.anioPublicacion.toString()
        view.findViewById<TextView>(R.id.tvSinopsis).text = novela.sinopsis
        view.findViewById<TextView>(R.id.tvUbicacion).text = novela.ubicacion

        val listViewResenas = view.findViewById<ListView>(R.id.listViewResenas)

        GlobalScope.launch(Dispatchers.IO) {
            val resenas = resenaDbHelper.obtenerResenasPorTitulo(novela.titulo).map { it.resena }
            withContext(Dispatchers.Main) {
                resenasAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resenas)
                listViewResenas.adapter = resenasAdapter
            }
        }

        view.findViewById<Button>(R.id.btnMarcarFavorita).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                novelaDbHelper.actualizarFavorito(novela.titulo, !novela.esFavorita)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<Button>(R.id.btnAgregarResena).setOnClickListener {
            (activity as? com.example.feedback6.actividades.MainActivity)?.mostrarAgregarResenaFragment(novela)
        }

        view.findViewById<Button>(R.id.btnVolverLista).setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        view.findViewById<Button>(R.id.btnEliminarNovela).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                novelaDbHelper.eliminarNovela(novela.titulo)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Novela eliminada", Toast.LENGTH_SHORT).show()
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        }

        return view
    }

    companion object {
        fun newInstance(novela: Novela): DetallesNovelaFragment {
            val fragment = DetallesNovelaFragment()
            val args = Bundle()
            args.putSerializable("novela", novela)
            fragment.arguments = args
            return fragment
        }
    }
}
