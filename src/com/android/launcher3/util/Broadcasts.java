package com.android.launcher3.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author tic
 * created on 18-10-24
 */
public class Broadcasts {

    public static void registerLocal(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager local = LocalBroadcastManager.getInstance(context);
        local.registerReceiver(receiver, filter);
    }

    public static void unRegisterLocal(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager local = LocalBroadcastManager.getInstance(context);
        local.unregisterReceiver(receiver);
    }

    public static void sendLocalBro(Context context, Intent intent) {
        LocalBroadcastManager local = LocalBroadcastManager.getInstance(context);
        local.sendBroadcast(intent);
    }

}
