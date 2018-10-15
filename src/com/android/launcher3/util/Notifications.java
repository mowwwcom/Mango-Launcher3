package com.android.launcher3.util;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.android.launcher3.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author tic
 * created on 18-10-15
 */
public class Notifications {

    /**
     * 跳转到设置开启通知权限
     *
     * @param context
     */
    public static void request(Context context) {
        Intent intent = new Intent();
        if (Utilities.ATLEAST_OREO) {
            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        } else {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static boolean isNotificationEnabled(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Utilities.ATLEAST_NOUGAT) {
            if (notificationManager != null) {
                return notificationManager.areNotificationsEnabled();
            }
        } else {
            return isNotificationAccess(context);
        }

        return true;
    }

    /**
     * 显示通知栏
     *
     * @param intent  点击消息跳转intent
     * @param ticker  提醒的文本
     * @param title   标题
     * @param content 内容
     */
    public static void showNotification(Context context, Intent intent, int id, int largeIcon,
                                        int smallIcon, String ticker, String title, String content) {
        showNotification(context, intent, id, ticker, title, content, largeIcon, smallIcon, Notification.DEFAULT_SOUND, -1);
    }

    /**
     * 显示通知栏
     *
     * @param intent  点击消息跳转intent
     * @param ticker  提醒的文本
     * @param title   标题
     * @param content 内容
     */
    public static void showNotificationProgress(Context context, Intent intent, int id, int largeIcon,
                                                int smallIcon, String ticker, String title, String content, int progress) {
        showNotification(context, intent, id, ticker, title, content, largeIcon, smallIcon, Notification.DEFAULT_SOUND, progress);
    }

    /**
     * 显示通知栏
     *
     * @param intent    点击消息跳转intent
     * @param ticker    提醒的文本
     * @param title     标题
     * @param content   内容
     * @param largeIcon 大图
     * @param smallIcon 小图
     */
    public static void showNotification(Context context, Intent intent, int id, String ticker, String title, String content,
                                        int largeIcon, int smallIcon, int defaults, int progress) {
        //1.从系统服务中获得通知管理器
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // notification build
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        // 构建 PendingIntent
        PendingIntent pi = null;
        if (intent != null) {
            pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        mBuilder.setTicker(ticker);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        //版本兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(smallIcon);
            mBuilder.setColor(Color.parseColor("#33CFC8"));
        } else {
            mBuilder.setPriority(Notification.PRIORITY_HIGH);//高优先级
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
        }
        if (progress >= 0) {
            mBuilder.setProgress(100, progress, false);
        }
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setAutoCancel(true);//点击后取消
        mBuilder.setDefaults(defaults);
        mBuilder.setWhen(System.currentTimeMillis());//设置通知时间
        if (pi != null) {
            mBuilder.setContentIntent(pi);
        }

        if (mNotificationManager != null) {
            mNotificationManager.notify(id, mBuilder.build());
        }
    }

    /**
     * 取消显示通知
     */
    public static void dismissNotification(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);//得到系统通知服务
        if (manager != null) {
            manager.cancel(id);
        }
    }

    private static boolean isNotificationAccess(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException
                | NoSuchMethodException
                | NoSuchFieldException
                | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

}
