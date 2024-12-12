package com.example.feedback6.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.feedback6.dataClasses.Novela

class NovelaAdapter(context: Context, novelas: List<Novela>) :
    ArrayAdapter<Novela>(context, android.R.layout.simple_spinner_item, novelas) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = getItem(position)?.titulo
        return view
    }

    override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = getItem(position)?.titulo
        return view
    }
}
