package com.example.feedback6.adapters

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.feedback6.R
import com.example.feedback6.dataClasses.Novela
import com.example.feedback6.fragments.DetallesNovelaFragment

class NovelaAdapter(
    private val context: Context,
    private val novelas: List<Novela>
) : ArrayAdapter<Novela>(context, 0, novelas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val novela = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_novela, parent, false)

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo)
        val tvEstrella = view.findViewById<TextView>(R.id.tvEstrella)
        val tvAutor = view.findViewById<TextView>(R.id.tvAutor)

        // Configuración de título y visibilidad de estrella si es favorita
        if (novela?.esFavorita == true) {
            val spannableTitle = SpannableString(novela.titulo).apply {
                setSpan(UnderlineSpan(), 0, novela.titulo.length, 0)
            }
            tvTitulo.text = spannableTitle
            tvEstrella.visibility = View.VISIBLE
        } else {
            tvTitulo.text = novela?.titulo
            tvEstrella.visibility = View.GONE
        }

        tvAutor.text = novela?.autor

        view.setOnClickListener {
            // Verificar que el contexto sea una FragmentActivity para cargar el fragmento
            if (context is FragmentActivity) {
                novela?.let {
                    val detallesFragment = DetallesNovelaFragment.newInstance(it)
                    context.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detallesFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        return view
    }
}
