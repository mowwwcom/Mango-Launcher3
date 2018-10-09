package com.android.launcher3.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.android.launcher3.R;
import com.android.launcher3.ui.BaseCompatActivity;

/**
 * @author tic
 * created on 18-10-9
 */
public class MangoSettingsActivity extends BaseCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new MangoSettingsFragment())
                    .commit();
        }
    }

    public static class MangoSettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            setPreferencesFromResource(R.xml.mango_settings, null);
        }
    }
}
