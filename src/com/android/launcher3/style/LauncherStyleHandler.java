package com.android.launcher3.style;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.preference.Preference;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.R;
import com.android.launcher3.util.OverrideApplyHandler;

import static com.android.launcher3.Utilities.getDevicePrefs;

/**
 * @author tic
 * created on 18-10-11
 */
public class LauncherStyleHandler {
    public static final String KEY_PREFERENCE = "pref_launcher_style";
    public static final int STYLE_STANDARD = 0;
    public static final int STYLE_DRAWER = 1;
    public static boolean isDrawer = false;

    public static String getAppliedValue(Context context) {
        String value = getDevicePrefs(context).getString(KEY_PREFERENCE, String.valueOf(STYLE_DRAWER));
//        isDrawer = value == STYLE_DRAWER;
//        return value;
        isDrawer = false;
        return String.valueOf(STYLE_STANDARD);
    }

    public static boolean isSupported(Activity activity) {
        return true;
    }

    public static void handlePreferenceUI(LauncherStylePreference preference) {
        Context context = preference.getContext();
        String value = getAppliedValue(context);
        preference.setValue(value);
        preference.setSummary((Integer.valueOf(value) == STYLE_DRAWER) ? R.string.launcher_style_drawer : R.string.launcher_style_standard);
        preference.setOnPreferenceChangeListener(new LauncherStyleHandler.PreferenceChangeHandler(context));
    }

    private static class PreferenceChangeHandler implements Preference.OnPreferenceChangeListener {

        private final Context mContext;

        PreferenceChangeHandler(Context context) {
            this.mContext = context;
        }

        @Override
        @SuppressLint("ApplySharedPref")
        public boolean onPreferenceChange(Preference preference, Object o) {
            String newValue = (String) o;
            if (getAppliedValue(mContext) != newValue) {
                // Value has changed
                ProgressDialog.show(mContext,
                        null /* title */,
                        mContext.getString(R.string.launcher_style_override_progress),
                        true /* indeterminate */,
                        false /* cancelable */);
                OverrideApplyHandler.applyWith(mContext, () -> {
                    // Synchronously write the preference.
                    getDevicePrefs(mContext).edit().putString(KEY_PREFERENCE, newValue).commit();
                    // Clear the icon cache.
                    LauncherAppState.getInstance(mContext).getIconCache().clear();
                });
            }
            return false;
        }
    }

}
