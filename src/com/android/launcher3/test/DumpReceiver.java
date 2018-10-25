package com.android.launcher3.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.launcher3.LauncherFiles;
import com.android.launcher3.util.IOUtils;

import java.io.File;

/**
 * @author tic
 * created on 18-10-25
 */
public class DumpReceiver extends BroadcastReceiver {

    public static final String ACTION_DUMP_DB = "com.mango.launcher.DUMP_DB";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }
        switch (action) {
            case ACTION_DUMP_DB:
                File file = context.getDatabasePath(LauncherFiles.LAUNCHER_DB);
                if (file.exists()) {
                    IOUtils.copy(file, new File("/sdcard/download/launcher3/"+ LauncherFiles.LAUNCHER_DB));
                }
                break;
        }
    }
}
