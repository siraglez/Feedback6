package com.example.feedback6.widgets

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.feedback6.R
import kotlinx.coroutines.runBlocking

class NovelasFavoritasWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: android.content.Intent): RemoteViewsFactory {
        return NovelasFavoritasRemoteViewsFactory(applicationContext)
    }
}

class NovelasFavoritasRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private var novelasFavoritas = listOf<String>()
    private val novelaDao by lazy { DatabaseProvider.getDatabase(context).novelaDao() }

    override fun onCreate() {}

    override fun onDataSetChanged() {
        runBlocking {
            novelasFavoritas = novelaDao.obtenerNovelasFavoritas().map { it.titulo }
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = novelasFavoritas.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_item_novela)
        views.setTextViewText(R.id.widget_novela_titulo, novelasFavoritas[position])

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}