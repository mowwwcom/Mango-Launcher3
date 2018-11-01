package com.android.launcher3.widget.custom;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * @author tic
 * created on 18-10-30
 */
public class SearchbarWidget extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
//        for (int i = 0; i < N; i++) {
//            int appWidgetId = appWidgetIds[i];
//
//            // Get the layout for the App Widget and attach an on-click listener
//            // to the button
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_search_bar);
//            views.setOnClickPendingIntent(R.id.ll_search, pendingIntent);
//
//            // Tell the AppWidgetManager to perform an update on the current app widget
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
    }
}
