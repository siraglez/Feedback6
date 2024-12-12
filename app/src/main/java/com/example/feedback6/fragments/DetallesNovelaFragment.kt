package com.example.feedback6.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.example.feedback6.actividades.MainActivity
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper
import com.example.feedback6.dataClasses.Novela

class DetallesNovelaFragment : Fragment() {

    private lateinit var novela: Novela
    private lateinit var novelaDbHelper: NovelaDatabaseHelper
    private lateinit var resenasAdapter: ArrayAdapter<String>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE)
        aplicarTema()

        arguments?.let {
            novela = it.getSerializable("novela") as Novela
        }
        novelaDbHelper = NovelaDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalles_novela, container, false)

        view.findViewById<TextView>(R.id.tvTitulo).text = novela.titulo
        view.findViewById<TextView>(R.id.tvAutor).text = novela.autor
        view.findViewById<TextView>(R.id.tvAnio).text = novela.anioPublicacion.toString()
        view.findViewById<TextView>(R.id.tvSinopsis).text = novela.sinopsis
        view.findViewById<TextView>(R.id.tvUbicacion).text = novela.ubicacion

        // Configuración del ListView de reseñas
        val listViewResenas = view.findViewById<ListView>(R.id.listViewResenas)
        resenasAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, obtenerResenas())
        listViewResenas.adapter = resenasAdapter

        view.findViewById<Button>(R.id.btnMarcarFavorita).setOnClickListener {
            novela.esFavorita = !novela.esFavorita
            novelaDbHelper.actualizarFavorito(novela.titulo, novela.esFavorita)
            Toast.makeText(context, if (novela.esFavorita) "Marcada como favorita" else "Desmarcada como favorita", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btnAgregarResena).setOnClickListener {
            (activity as? MainActivity)?.mostrarAgregarResenaFragment(novela)
        }

        view.findViewById<Button>(R.id.btnVolverLista).setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        view.findViewById<Button>(R.id.btnEliminarNovela).setOnClickListener {
            novelaDbHelper.eliminarNovela(novela.titulo)
            Toast.makeText(context, "Novela eliminada", Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.popBackStack()
        }

        return view
    }

    private fun obtenerResenas(): List<String> {
        return novelaDbHelper.obtenerResenasPorTitulo(novela.titulo)
    }

    private fun aplicarTema() {
        val temaOscuro = sharedPreferences.getBoolean("temaOscuro", false)
        requireContext().setTheme(if (temaOscuro) R.style.Theme_Feedback6_Night else R.style.Theme_Feedback6_Day)
    }

    override fun onResume() {
        super.onResume()
        // Actualizar la lista de reseñas cada vez que el fragmento se reanuda
        resenasAdapter.clear()
        resenasAdapter.addAll(obtenerResenas())
        resenasAdapter.notifyDataSetChanged()
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
