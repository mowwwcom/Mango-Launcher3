package com.android.launcher3.util.system;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.CalendarContract;

/**
 * @author tic
 * created on 18/10/28.
 */

public class Activities {
    private static final String TAG = "Activities";

    public static void goTo(Context context, String action) {
        Intent intent = new Intent(action);
        context.startActivity(intent);
    }

    public static void goTo(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static Intent calendar() {
        return new Intent(Intent.ACTION_VIEW, CalendarContract.CONTENT_URI.buildUpon().appendPath("time").build());
    }

    public static Intent clock() {
        return new Intent(AlarmClock.ACTION_SET_ALARM);
    }
}
