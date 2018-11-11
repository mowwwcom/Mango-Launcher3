package com.android.launcher3.util.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author tic
 *         created on 18/11/11.
 */

public class Alarms {
    private static final String TAG = Alarms.class.getSimpleName();

    public void setWindow(Context context, String action) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager == null) {
            Log.e(TAG, "Alarm set failed");
            return;
        }
        //API大于19的时候使用
        manager.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0,
                60 * 1000,
                PendingIntent.getBroadcast(context,
                        0,
                        new Intent(action),
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
