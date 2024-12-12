package com.example.feedback6.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.feedback6.R
import com.example.feedback6.actividades.MainActivity
import com.example.feedback6.baseDeDatos.NovelaDatabaseHelper

class NovelasFavoritasWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return NovelasFavoritasRemoteViewsFactory(applicationContext)
    }
}

class NovelasFavoritasRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var novelasFavoritas = listOf<String>()
    private lateinit var dbHelper: NovelaDatabaseHelper

    override fun onCreate() {
        dbHelper = NovelaDatabaseHelper(context)
    }

    override fun onDataSetChanged() {
        novelasFavoritas = dbHelper.obtenerNovelasFavoritas().map { it.titulo }
    }

    override fun onDestroy() {
        dbHelper.close()
    }

    override fun getCount(): Int = novelasFavoritas.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_item_novela)
        views.setTextViewText(R.id.widget_novela_titulo, novelasFavoritas[position])

        //Crear un Intent para abrir la actividad principal y pasar la info al fragmento de detalles
        val novela = novelasFavoritas[position]
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("novela", novela)
        }

        // Crear un PendingIntent para la acción
        val pendingIntent = PendingIntent.getActivity(context, position, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Establecer el PendingIntent en la vista
        views.setOnClickPendingIntent(R.id.widget_novela_titulo, pendingIntent)

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
