package com.android.launcher3.widget.custom;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.android.launcher3.R;
import com.android.launcher3.util.DateTimes;
import com.android.launcher3.util.system.Activities;
import com.android.launcher3.util.system.Alarms;
import com.mango.launcher3.receiver.AlarmsReceiver;

import java.util.Date;

/**
 * @author tic
 * created on 18-11-8
 */
public class ClockWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateTime(context, appWidgetManager);
    }

    public static void initClock(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, ClockWidget.class));
        for (int id : ids) {
            AppWidgetProviderInfo info = manager.getAppWidgetInfo(id);
            if (info != null) {
                Alarms.setWindow(context, AlarmsReceiver.clockIntent(context));
                break;
            }
        }
    }

    public static void updateTime(Context context, AppWidgetManager appWidgetManager) {
        ComponentName component = new ComponentName(context, ClockWidget.class);
        CharSequence bestPattern = DateTimes.getBestDateTimePattern(context);

        PendingIntent clockIntent = PendingIntent
                .getActivity(context, 0, Activities.clock(), 0);
        PendingIntent calendarIntent = PendingIntent
                .getActivity(context, 0, Activities.calendar(), 0);
        int[] ids = appWidgetManager.getAppWidgetIds(component);
        for (int id : ids) {
            AppWidgetProviderInfo info = appWidgetManager.getAppWidgetInfo(id);
            if (info.provider.getClassName().equals(ClockWidget.class.getName())) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_clock);

                Bundle options = appWidgetManager.getAppWidgetOptions(id);
                int appWidgetMinHeight = options.getInt("appWidgetMinHeight");
                if (appWidgetMinHeight <= 48) {
                    views.setViewVisibility(R.id.time, View.GONE);
                } else {
                    String time = DateTimes.convertDateToString(new Date(), DateTimes.DATETIME_FORMAT6);
                    views.setViewVisibility(R.id.time, View.VISIBLE);
                    views.setCharSequence(R.id.time, "setText", time);
                }
                views.setCharSequence(R.id.date1, "setText", bestPattern);
                views.setViewVisibility(R.id.weather, View.GONE);
                views.setViewVisibility(R.id.split, View.GONE);

                views.setOnClickPendingIntent(R.id.date1, calendarIntent);
                views.setOnClickPendingIntent(R.id.time, clockIntent);
                appWidgetManager.updateAppWidget(component, views);
                break;
            }
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager,
                                          int appWidgetId,
                                          Bundle newOptions) {
//        Log.d("widget", "appwidgetId:" + appWidgetId);
//        Set<String> keys = newOptions.keySet();
//        appWidgetManager.getAppWidgetOptions(appWidgetId);
//        for (String key : keys) {
//            Log.e("widget", key + " ==> " + newOptions.get(key).toString());
//        }
        updateTime(context, appWidgetManager);
    }
}
