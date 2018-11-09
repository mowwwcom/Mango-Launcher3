package com.android.launcher3.widget.custom;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.launcher3.R;

/**
 * @author tic
 * created on 18-11-8
 */
public class ClockWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Perform this loop procedure for each App Widget that belongs to this provider
        int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_clock);
//            views.setString(R.id.date1, "", "");
//            views.setString(R.id.time, "", "");
            views.setViewVisibility(R.id.weather, View.GONE);
            views.setViewVisibility(R.id.split, View.GONE);
            // views.setViewVisibility(R.id.time, View.GONE);

            PendingIntent clockIntent = PendingIntent.getActivity(context, 0, new Intent(AlarmClock.ACTION_SET_ALARM), 0);
            views.setOnClickPendingIntent(R.id.date1, clockIntent);
            // views.setOnClickPendingIntent(R.id.weather, PendingIntent.getActivity());
            views.setOnClickPendingIntent(R.id.time, clockIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    /**
     * Called in response to the {@link AppWidgetManager#ACTION_APPWIDGET_OPTIONS_CHANGED}
     * broadcast when this widget has been layed out at a new size.
     * <p>
     * {@more}
     *
     * @param context          The {@link android.content.Context Context} in which this receiver is
     *                         running.
     * @param appWidgetManager A {@link AppWidgetManager} object you can call {@link
     *                         AppWidgetManager#updateAppWidget} on.
     * @param appWidgetId      The appWidgetId of the widget whose size changed.
     * @param newOptions       The newOptions of the widget whose size changed.
     * @see AppWidgetManager#ACTION_APPWIDGET_OPTIONS_CHANGED
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.e("widget", "appwidgetId:" + appWidgetId);
        Log.e("widget", "newOptions:" + newOptions.toString());
    }
}
