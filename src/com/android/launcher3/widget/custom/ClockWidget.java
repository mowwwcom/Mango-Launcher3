package com.android.launcher3.widget.custom;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.util.system.Activities;

import java.util.Locale;

/**
 * @author tic
 * created on 18-11-8
 */
public class ClockWidget extends AppWidgetProvider {

    private static final String ACTION_UPDATE_TIME = "android.appwidget.action.APPWIDGET_UPDATETIME";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (action.equals(ACTION_UPDATE_TIME)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            CharSequence date = getBastDatePattern(context);
            updateTime(context, appWidgetManager, date);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Perform this loop procedure for each App Widget that belongs to this provider
        CharSequence date = getBastDatePattern(context);
        updateTime(context, appWidgetManager, date);
    }

    private void updateTime(Context context, AppWidgetManager appWidgetManager,
                            CharSequence date) {
        PendingIntent clockIntent = PendingIntent
                .getActivity(context, 0, Activities.clock(), 0);
        PendingIntent calendarIntent = PendingIntent
                .getActivity(context, 0, Activities.calendar(), 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_clock);
        views.setCharSequence(R.id.date1, "setText", date);
        views.setViewVisibility(R.id.weather, View.GONE);
        views.setViewVisibility(R.id.split, View.GONE);
        views.setViewVisibility(R.id.time, View.GONE);

        views.setOnClickPendingIntent(R.id.date1, calendarIntent);
        views.setOnClickPendingIntent(R.id.time, clockIntent);
        appWidgetManager.updateAppWidget(new ComponentName(context, ClockWidget.class), views);
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

    public CharSequence getBastDatePattern(Context context) {
        Locale locale;
        if (Utilities.ATLEAST_NOUGAT) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        long now = System.currentTimeMillis();
        String format = DateFormat.getBestDateTimePattern(locale, "EEEEMMMMd");
        return DateFormat.format(format, now);
    }
}
