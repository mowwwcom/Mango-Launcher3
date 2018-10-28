package com.android.launcher3.util.system;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.android.launcher3.BuildConfig;
import com.android.launcher3.LauncherM;

import java.lang.reflect.Field;

/**
 * @author tic
 *         created on 18/10/28.
 */
public class LauncherReset {
    private static final ComponentName LAUNCHER =
            new ComponentName(BuildConfig.APPLICATION_ID, LauncherM.class.getName());

    private static final ComponentName[] RESOLVERS = {
            ComponentName.unflattenFromString("android/com.android.internal.app.ResolverActivity"),
            ComponentName.unflattenFromString("com.huawei.android.internal.app/.HwResolverActivity"),
            ComponentName.unflattenFromString("android/com.android.internal.app.ResolverActivityEx")};

    public static void goToLauncherSetting(Context context) {
        boolean isHuaWei = false;
        if ((Build.VERSION.SDK_INT >= 21)
                && ("HUAWEI".equalsIgnoreCase(Build.BRAND))
                && (getHwFlag() != 0)) {
            isHuaWei = true;
        }

        Intent intent;
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        if (isHuaWei) {
            intent = new Intent("com.android.settings.PREFERRED_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (packageManager.resolveActivity(intent, 0) != null) {
                context.startActivity(intent);
                return;
            }
        }
        packageManager.setComponentEnabledSetting(LAUNCHER,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        if (getTargetComponentName(packageManager) != null) {
            packageManager.setComponentEnabledSetting(LAUNCHER,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            intent = new Intent("android.settings.HOME_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }
        intent = getHomeIntent();
        intent.putExtra("homereset_return_to_settings", true);

        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        packageManager.setComponentEnabledSetting(LAUNCHER,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        context.startActivity(intent);
    }

    private static Intent getHomeIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        return intent;
    }

    private static ComponentName getTargetComponentName(PackageManager pm) {
        ComponentName target = getHomeIntent().resolveActivity(pm);
        for (ComponentName cn : RESOLVERS) {
            if (cn.equals(target)) {
                return target;
            }
        }
        return null;
    }

//    public static ComponentName getTargetComponentName(PackageManager pm) {
//        int k = 0;
//        ComponentName name;
//        try {
//            name = getHomeIntent().resolveActivity(pm);
//            int i = 0;
//            for (; ; ) {
//                int j = k;
//                if (i < RESOLVERS.length) {
//                    boolean bool = RESOLVERS[i].equals(name);
//                    if (bool) {
//                        j = 1;
//                    } else {
//                        i += 1;
//                        continue;
//                    }
//                }
//                if (j == 0) {
//                    return name;
//                }
//                return null;
//            }
//        } catch (Exception localException2) {
//            name = null;
//        }
//        return name;
//    }

    private static int getHwFlag() {
        try {
            Class<Intent> obj = Intent.class;
            Field field = obj.getField("FLAG_HW_HOME_INTENT_FROM_SYSTEM");
            field.setAccessible(true);
            return field.getInt(obj);
        } catch (Exception e) {
        }
        return 0;
    }
}
