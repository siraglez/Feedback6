package com.example.feedback6.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.feedback6.R

class NovelasFavoritasWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_novelas_favoritas)

            // Configurar el RemoteViewsService para cargar la lista
            val intent = Intent(context, NovelasFavoritasWidgetService::class.java)
            views.setRemoteAdapter(R.id.widget_list, intent)

            // Actualizar el widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}