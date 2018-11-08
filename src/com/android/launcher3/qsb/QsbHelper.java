package com.android.launcher3.qsb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.R;
import com.android.launcher3.util.OverrideApplyHandler;

import static com.android.launcher3.Utilities.getDevicePrefs;

/**
 * @author tic
 *         created on 18-10-10
 */
public class QsbHelper {
    public static final String KEY_PREFERENCE = "pref_qsb_position";
    public static final int POSITION_NONE = -1;
    public static final int POSITION_TOP = 0;
    public static final int POSITION_BOTTOM = 1;
    public static final int POSITION_HOT_SEAT = 2;

    private static Boolean inHotseat = null;

    public static boolean isSupported(Activity activity) {
        return true;
    }

    public static int getAppliedValue(Context context) {
//        return getDevicePrefs(context).getInt(KEY_PREFERENCE, POSITION_TOP);
        return POSITION_HOT_SEAT;
    }

    public static boolean inHotSeat(Context context) {
        if (inHotseat == null) {
            inHotseat = getAppliedValue(context) == POSITION_HOT_SEAT;
        }
        return inHotseat;
    }

    public static void handlePreferenceUI(ListPreference preference) {
        Context context = preference.getContext();
        preference.setValue(String.valueOf(getAppliedValue(context)));
        preference.setOnPreferenceChangeListener(new QsbHelper.PreferenceChangeHandler(context));
    }

    private static class PreferenceChangeHandler implements Preference.OnPreferenceChangeListener {

        private final Context mContext;

        PreferenceChangeHandler(Context context) {
            this.mContext = context;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            int newValue = Integer.valueOf((String) o);
            if (getAppliedValue(mContext) != newValue) {
                // Value has changed
                ProgressDialog.show(mContext,
                        null /* title */,
                        mContext.getString(R.string.qsb_position_override_progress),
                        true /* indeterminate */,
                        false /* cancelable */);
                OverrideApplyHandler.applyWith(mContext, () -> {
                    // Synchronously write the preference.
                    getDevicePrefs(mContext).edit().putInt(KEY_PREFERENCE, newValue).commit();
                    // Clear the icon cache.
                    LauncherAppState.getInstance(mContext).getIconCache().clear();
                });
            }
            return false;
        }
    }
}
