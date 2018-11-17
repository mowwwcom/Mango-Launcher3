package com.android.launcher3.util.security;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author tic
 * created on 18-6-27
 */

public final class PermissionManager {

    private Callback mCallback;
    private String[] unauthorizedPermission;

    public PermissionManager(Callback callback) {
        this.mCallback = Preconditions.checkNotNull(callback, "Callback cannot be null");
    }

    public interface Callback {
        /**
         * get contenxt
         */
        Activity getActivity();

        /**
         * permission refused
         *
         * @param permissions p
         * @param accept      accept
         */
        void onPermissionResult(@NonNull String permissions, boolean accept);
    }

    /**
     * 请求获取权限
     */
    public void requestPermission() {
        Security permissions = mCallback.getActivity().getClass().getAnnotation(Security.class);
        if (permissions == null) {
            Log.d("PermissionManager", "no permission request");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permission = permissions.value();
            if (!havePermission(permission)) {
                mCallback.getActivity().requestPermissions(this.unauthorizedPermission, 0x123);
            }
        }
    }

    /**
     * 检查已经申请到的权限，并保存未授权的权限，再次申请
     *
     * @param permissions
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean havePermission(String... permissions) {
        ArrayList<String> permissionList = new ArrayList<>();

        for (String permission : permissions) {
            if (mCallback.getActivity()
                    .checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            } else {
                mCallback.onPermissionResult(permission, true);
            }
        }
        if (permissionList.size() > 0) {
            this.unauthorizedPermission = permissionList.toArray(new String[permissionList.size()]);
            Log.d("PermissionManager", "尚未赋予的权限:" + Arrays.toString(unauthorizedPermission));
            return false;
        }

        return true;
    }

    /**
     * 权限申请结果通知，需要在Activity onRequestPermissionResult方法中调用
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                mCallback.onPermissionResult(permissions[i], false);
            } else {
                mCallback.onPermissionResult(permissions[i], true);
            }
        }
    }
}
