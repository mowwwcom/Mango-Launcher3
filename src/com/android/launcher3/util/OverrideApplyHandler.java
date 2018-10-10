package com.android.launcher3.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.android.launcher3.LauncherModel;
import com.android.launcher3.Utilities;

/**
 * @author tic
 * created on 18-10-10
 */
public class OverrideApplyHandler implements Runnable {

    private final Context mContext;
    /**
     * Time to wait before killing the process this ensures that the progress bar is visible for
     * sufficient time so that there is no flicker.
     **/
    private static final long PROCESS_KILL_DELAY_MS = 1000;
    /**
     * the answer to everything
     */
    private static final int RESTART_REQUEST_CODE = 42;
    private final Runnable mRunnable;

    private OverrideApplyHandler(Context context, Runnable r) {
        mContext = context;
        mRunnable = r;
    }

    public static void applyWith(Context context, Runnable r) {
        new LooperExecutor(LauncherModel.getWorkerLooper()).execute(
                new OverrideApplyHandler(context, r));
    }

    @Override
    public void run() {
        if (mRunnable != null) {
            mRunnable.run();
        }
        // Wait for it
        try {
            Thread.sleep(PROCESS_KILL_DELAY_MS);
        } catch (Exception e) {
            Log.e(OverrideApplyHandler.class.getSimpleName(), "Error waiting", e);
        }

        // Schedule an alarm before we kill ourself.
        Intent homeIntent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .setPackage(mContext.getPackageName())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(mContext, RESTART_REQUEST_CODE,
                homeIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = Utilities.ATLEAST_MARSHMALLOW
                ? mContext.getSystemService(AlarmManager.class)
                : (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        manager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 50, pi);

        // Kill process
        Process.killProcess(Process.myPid());
    }
}
