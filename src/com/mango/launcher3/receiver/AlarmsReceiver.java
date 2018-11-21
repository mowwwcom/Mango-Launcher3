package com.mango.launcher3.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.launcher3.util.system.Alarms;
import com.android.launcher3.widget.custom.ClockWidget;

/**
 * @author tic
 * created on 18-11-19
 */
public class AlarmsReceiver extends BroadcastReceiver {

    public static final String ACTION_UPDATE_TIME = "android.appwidget.action.APPWIDGET_UPDATETIME";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        Log.d("AlarmsReceiver", "action:" + intent.getAction());
        final String action = intent.getAction();
        switch (action) {
            case ACTION_UPDATE_TIME:
                // 更新时间widget
                ClockWidget.updateTime(context, AppWidgetManager.getInstance(context));
                // repeat
                Alarms.setWindow(context, clockIntent(context));
                break;
            default:
                break;
        }
    }

    public static Intent clockIntent(Context context) {
        Intent intent = new Intent(ACTION_UPDATE_TIME);
        intent.setClass(context, AlarmsReceiver.class);
        return intent;
    }
}
