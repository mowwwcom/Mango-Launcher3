package com.android.launcher3.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;

import com.android.launcher3.Utilities;
import com.android.launcher3.util.Broadcasts;

/**
 * @author tic
 * created on 18-10-24
 */
public class SmartAssistants {

    public static final String PREFERENCE_KEY_SMART_ASSISTANT = "pref_add_dashboard_to_home";
    public static final String ACTION_ENABLE_SMART_ASSISTANT = "action_enable_smart_assistant";

    public static boolean isEnabled(Context context) {
//        return Utilities.getDevicePrefs(context).getBoolean(PREFERENCE_KEY_SMART_ASSISTANT, true);
        return false;
    }

    public static void handlePreferenceUI(SwitchPreference preference) {
        boolean value = isEnabled(preference.getContext());
        preference.setDefaultValue(value);
        preference.setChecked(value);
        preference.setOnPreferenceChangeListener(new PreferenceChangeHandler(preference.getContext()));
    }

    static class PreferenceChangeHandler implements Preference.OnPreferenceChangeListener {
        private final Context mContext;

        PreferenceChangeHandler(Context context) {
            this.mContext = context;
        }

        @Override
        @SuppressLint("ApplySharedPref")
        public boolean onPreferenceChange(Preference preference, Object o) {
            boolean newValue = (Boolean) o;
            if (isEnabled(mContext) != newValue) {
                Utilities.getDevicePrefs(mContext)
                        .edit()
                        .putBoolean(PREFERENCE_KEY_SMART_ASSISTANT, newValue)
                        .commit();
                preference.setDefaultValue(newValue);
                ((SwitchPreference) preference).setChecked(newValue);

                // remove custom screen
                Intent intent = new Intent(ACTION_ENABLE_SMART_ASSISTANT);
                Broadcasts.sendLocalBro(mContext, intent);
            }
            return false;
        }
    }
}
