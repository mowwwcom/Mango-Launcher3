package com.android.launcher3;

import android.app.Application;

import com.android.launcher3.qsb.QsbHelper;
import com.android.launcher3.style.LauncherStyleHandler;

/**
 * @author tic
 * created on 18-10-12
 */
public class LauncherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // confirm launcher style
        LauncherStyleHandler.getAppliedValue(this);
        QsbHelper.inHotSeat(this);
    }
}
