package com.android.launcher3.util;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;

import com.android.launcher3.R;
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
    public static void showNotification(Context context, PendingIntent intent, int id, int largeIcon,
                                        int smallIcon, String ticker, String title, String content) {
        showNotification(context, intent, id, ticker, title, content, largeIcon, smallIcon,
                Notification.DEFAULT_SOUND, -1, context.getPackageName());
    }

    /**
     * 显示通知栏
     *
     * @param intent  点击消息跳转intent
     * @param ticker  提醒的文本
     * @param title   标题
     * @param content 内容
     */
    public static void showNotificationProgress(Context context, PendingIntent intent, int id, int largeIcon,
                                                int smallIcon, String ticker, String title, String content, int progress) {
        showNotification(context, intent, id, ticker, title, content, largeIcon, smallIcon,
                Notification.DEFAULT_SOUND, progress, context.getPackageName());
    }

    /**
     * 显示通知栏
     *
     * @param pi        点击消息跳转intent
     * @param ticker    提醒的文本
     * @param title     标题
     * @param content   内容
     * @param largeIcon 大图
     * @param smallIcon 小图
     */
    public static void showNotification(Context context, PendingIntent pi, int id,
                                        String ticker, String title, String content,
                                        int largeIcon, int smallIcon, int defaults, int progress, String group) {
        String channelId = context.getPackageName() + "-" + id;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        assert manager != null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setChannelId(channelId)
                .setDefaults(defaults)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setGroup(group)
                .setColorized(true)
                .setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null));

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));

        if (pi != null) {
            builder.setContentIntent(pi);
        }

        if (progress >= 0) {
            builder.setProgress(100, progress, false);
        }

        if (Utilities.ATLEAST_P) {
            // 9.0
        } else if (Utilities.ATLEAST_OREO) {
            // 8.0
            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        } else if (Utilities.ATLEAST_NOUGAT) {
            // 7.0
            builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        } else if (Utilities.ATLEAST_MARSHMALLOW) {
            // 6.0
        } else if (Utilities.ATLEAST_LOLLIPOP) {
            // 5.0
            builder.setPriority(Notification.PRIORITY_HIGH);
            // 当前需要隐藏隐私信息时，设置Visibility，并调用 PublicVersion 替换原有通知
            // builder.setVisibility(Notification.VISIBILITY_PRIVATE);
            // builder.setPublicVersion(privateNotify);
            // builder.addPerson()
        }
        manager.notify(id, builder.build());
    }

    /**
     * 取消显示通知
     */
    public static void dismissNotification(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
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
