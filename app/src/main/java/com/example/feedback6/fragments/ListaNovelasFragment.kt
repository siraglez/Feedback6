package com.example.feedback6.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.feedback6.R
import com.example.feedback6.actividades.MapaActivity
import com.example.feedback6.adapters.NovelaAdapter
import com.example.feedback6.baseDeDatos.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaNovelasFragment : Fragment() {
    private lateinit var listener: OnNovelaSelectedListener
    private lateinit var adapter: NovelaAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val novelaDao by lazy { DatabaseProvider.getDatabase(requireContext()).novelaDao() }

    interface OnNovelaSelectedListener {
        fun onNovelaSelected(novela: com.example.feedback6.dataClasses.Novela)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE)

        if (context is OnNovelaSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context debe implementar OnNovelaSelectedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lista_novelas, container, false)
        val listViewNovelas = view.findViewById<ListView>(R.id.listViewNovelas)
        val btnAgregarNovela = view.findViewById<Button>(R.id.btnAgregarNovela)
        val btnVerMapa = view.findViewById<Button>(R.id.btnVerMapa)

        GlobalScope.launch(Dispatchers.IO) {
            val novelas = novelaDao.obtenerNovelas()
            withContext(Dispatchers.Main) {
                adapter = NovelaAdapter(requireContext(), novelas)
                listViewNovelas.adapter = adapter
            }
        }

        listViewNovelas.setOnItemClickListener { _, _, position, _ ->
            val novela = adapter.getItem(position)
            novela?.let { listener.onNovelaSelected(it) }
        }

        btnAgregarNovela.setOnClickListener {
            (activity as? com.example.feedback6.actividades.MainActivity)?.mostrarAgregarNovelaFragment()
        }

        btnVerMapa.setOnClickListener {
            val intent = Intent(activity, MapaActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}