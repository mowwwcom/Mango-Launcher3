package com.android.launcher3.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherActivityInfo;
import android.os.UserHandle;
import android.util.ArrayMap;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.classify.ClassifyCache;
import com.android.launcher3.compat.LauncherAppsCompat;
import com.android.launcher3.compat.UserManagerCompat;

import java.util.List;

/**
 * @author tic
 * created on 18-10-22
 */
public class ClassifyModel {

    private final UserManagerCompat mUserManager;
    private final ClassifyCache mCache;
    private static final String TAG = ClassifyModel.class.getSimpleName();

    public ClassifyModel(LauncherAppState mApp) {
        mUserManager = UserManagerCompat.getInstance(mApp.getContext());
        mCache = new ClassifyCache();
    }

    public void init(Context context) {
        mCache.init();

        List<UserHandle> profiles = mUserManager.getUserProfiles();
        for (UserHandle user : profiles) {
            List<LauncherActivityInfo> mLauncherApps = LauncherAppsCompat
                    .getInstance(context)
                    .getActivityList(null, user);

            for (LauncherActivityInfo app : mLauncherApps) {
                boolean isSystem = isSystemApp(app.getApplicationInfo());
                if (isSystem) {
                    mCache.cacheSystem(app.getComponentName().getPackageName());
                }
            }
        }
    }

    public int getType(String packageName) {
        ArrayMap<String, Integer> cache = mCache.getCache();
        Integer type = cache.get(packageName);
        if (type == null) {
            type = 0;
        }
        return type;
    }

    public boolean isSystemApp(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}