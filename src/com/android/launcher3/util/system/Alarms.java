package com.android.launcher3.util.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

/**
 * @author tic
 * created on 18/11/11.
 */

public class Alarms {
    private static final String TAG = Alarms.class.getSimpleName();

    public static void setWindow(Context context, Intent intent) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (manager == null) {
            Log.e(TAG, "Alarm set failed");
            return;
        }
        int anHour = 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        //API大于19的时候使用, 时间不准确
//        manager.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                0,
//                60 * 1000,
//                PendingIntent.getBroadcast(context,
//                        0,
//                        intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
