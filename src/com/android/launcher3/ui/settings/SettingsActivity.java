/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.view.View;

import com.android.launcher3.LauncherFiles;
import com.android.launcher3.R;
import com.android.launcher3.SessionCommitReceiver;
import com.android.launcher3.Utilities;
import com.android.launcher3.dashboard.SmartAssistants;
import com.android.launcher3.graphics.IconShapeOverride;
import com.android.launcher3.notification.NotificationListener;
import com.android.launcher3.qsb.QsbHelper;
import com.android.launcher3.style.LauncherStyleDialogFragment;
import com.android.launcher3.style.LauncherStyleHandler;
import com.android.launcher3.style.LauncherStylePreference;
import com.android.launcher3.ui.BaseCompatActivity;
import com.android.launcher3.ui.user.UserEntryActivity;
import com.android.launcher3.util.SettingsObserver;
import com.android.launcher3.util.system.Activities;
import com.android.launcher3.util.system.LauncherReset;
import com.android.launcher3.views.ButtonPreference;

import static com.android.launcher3.states.RotationHelper.ALLOW_ROTATION_PREFERENCE_KEY;
import static com.android.launcher3.states.RotationHelper.getAllowRotationDefaultValue;

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 *
 * @author ticooops
 */
public class SettingsActivity extends BaseCompatActivity {

    private static final String ICON_BADGING_PREFERENCE_KEY = "pref_icon_badging";
    /**
     * Hidden field Settings.Secure.NOTIFICATION_BADGING
     */
    public static final String NOTIFICATION_BADGING = "notification_badging";
    /**
     * Hidden field Settings.Secure.ENABLED_NOTIFICATION_LISTENERS
     */
    private static final String NOTIFICATION_ENABLED_LISTENERS = "enabled_notification_listeners";

    private static final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
    private static final String EXTRA_SHOW_FRAGMENT_ARGS = ":settings:show_fragment_args";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showBackButton();
        setTitle(R.string.mango_settings);
        setDefaultLauncherVisible();
        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.setting_content, getNewFragment())
                    .commit();
        }
        findViewById(R.id.relative_layout).setOnClickListener(v -> {
            Activities.goTo(this, UserEntryActivity.class);
        });
    }

    @Override
    protected int layout() {
        return R.layout.activity_settings;
    }

    private void setDefaultLauncherVisible() {
        View view = findViewById(R.id.ll_set_default_launcher);
        boolean isHome = Utilities.isOurHome(this);
        view.setVisibility(isHome ? View.GONE : View.VISIBLE);
        if (!isHome) {
            view.setOnClickListener(v -> LauncherReset.goToLauncherSetting(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDefaultLauncherVisible();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected PreferenceFragmentCompat getNewFragment() {
        return new LauncherSettingsFragment();
    }

    /**
     * This fragment shows the launcher preferences.
     */
    public static class LauncherSettingsFragment extends PreferenceFragmentCompat {
        private IconBadgingObserver mIconBadgingObserver;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String s) {

            getPreferenceManager().setSharedPreferencesName(LauncherFiles.SHARED_PREFERENCES_KEY);
            addPreferencesFromResource(R.xml.launcher_preferences);

            ContentResolver resolver = getActivity().getContentResolver();

            ButtonPreference iconBadgingPref =
                    (ButtonPreference) findPreference(ICON_BADGING_PREFERENCE_KEY);
            if (!Utilities.ATLEAST_OREO) {
                getPreferenceScreen().removePreference(
                        findPreference(SessionCommitReceiver.ADD_ICON_PREFERENCE_KEY));
                getPreferenceScreen().removePreference(iconBadgingPref);
            } else if (!getResources().getBoolean(R.bool.notification_badging_enabled)) {
                getPreferenceScreen().removePreference(iconBadgingPref);
            } else {
                // Listen to system notification badge settings while this UI is active.
                mIconBadgingObserver = new IconBadgingObserver(
                        iconBadgingPref, resolver, getFragmentManager());
                mIconBadgingObserver.register(NOTIFICATION_BADGING, NOTIFICATION_ENABLED_LISTENERS);
            }

            Preference iconShapeOverride = findPreference(IconShapeOverride.KEY_PREFERENCE);
            if (iconShapeOverride != null) {
                if (IconShapeOverride.isSupported(getActivity())) {
                    IconShapeOverride.handlePreferenceUi((ListPreference) iconShapeOverride);
                } else {
                    getPreferenceScreen().removePreference(iconShapeOverride);
                }
            }

            // Setup allow rotation preference
            Preference rotationPref = findPreference(ALLOW_ROTATION_PREFERENCE_KEY);
            if (getResources().getBoolean(R.bool.allow_rotation)) {
                // Launcher supports rotation by default. No need to show this setting.
                getPreferenceScreen().removePreference(rotationPref);
            } else {
                // Initialize the UI once
                rotationPref.setDefaultValue(getAllowRotationDefaultValue());
            }

            // Setup qbs position preference
            Preference positionPref = findPreference(QsbHelper.KEY_PREFERENCE);
            if (positionPref != null) {
                if (QsbHelper.isSupported(getActivity())) {
                    QsbHelper.handlePreferenceUI((ListPreference) positionPref);
                }
            }

            Preference launcherStyle = findPreference(LauncherStyleHandler.KEY_PREFERENCE);
            if (launcherStyle != null) {
                if (LauncherStyleHandler.isSupported(getActivity())) {
                    LauncherStyleHandler.handlePreferenceUI((LauncherStylePreference) launcherStyle);
                }
            }

            Preference smartAssistant = findPreference(SmartAssistants.PREFERENCE_KEY_SMART_ASSISTANT);
            if (smartAssistant != null) {
                SmartAssistants.handlePreferenceUI((SwitchPreference) smartAssistant);
            }
        }

        @Override
        public void onDestroy() {
            if (mIconBadgingObserver != null) {
                mIconBadgingObserver.unregister();
                mIconBadgingObserver = null;
            }
            super.onDestroy();
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            DialogFragment f;
            if (preference instanceof LauncherStylePreference) {
                f = LauncherStyleDialogFragment.newInstance(LauncherStyleHandler.KEY_PREFERENCE);
                f.setTargetFragment(this, 0);
                f.show(this.getFragmentManager(), "LauncherStyle PreferenceFragment");
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }

        /**
         * Content observer which listens for system badging setting changes,
         * and updates the launcher badging setting subtext accordingly.
         */
        private static class IconBadgingObserver extends SettingsObserver.Secure
                implements Preference.OnPreferenceClickListener {

            private final ButtonPreference mBadgingPref;
            private final ContentResolver mResolver;
            private final FragmentManager mFragmentManager;

            public IconBadgingObserver(ButtonPreference badgingPref, ContentResolver resolver,
                                       FragmentManager fragmentManager) {
                super(resolver);
                mBadgingPref = badgingPref;
                mResolver = resolver;
                mFragmentManager = fragmentManager;
            }

            @Override
            public void onSettingChanged(boolean enabled) {
                int summary = enabled ? R.string.icon_badging_desc_on : R.string.icon_badging_desc_off;

                boolean serviceEnabled = true;
                if (enabled) {
                    // Check if the listener is enabled or not.
                    String enabledListeners =
                            Settings.Secure.getString(mResolver, NOTIFICATION_ENABLED_LISTENERS);
                    ComponentName myListener =
                            new ComponentName(mBadgingPref.getContext(), NotificationListener.class);
                    serviceEnabled = enabledListeners != null &&
                            (enabledListeners.contains(myListener.flattenToString()) ||
                                    enabledListeners.contains(myListener.flattenToShortString()));
                    if (!serviceEnabled) {
                        summary = R.string.title_missing_notification_access;
                    }
                }
                mBadgingPref.setWidgetFrameVisible(!serviceEnabled);
                mBadgingPref.setOnPreferenceClickListener(serviceEnabled ? null : this);
                mBadgingPref.setSummary(summary);

            }

            @Override
            public boolean onPreferenceClick(Preference preference) {
                new NotificationAccessConfirmation().show(mFragmentManager, "notification_access");
                return true;
            }
        }

        public static class NotificationAccessConfirmation
                extends android.support.v4.app.DialogFragment implements DialogInterface.OnClickListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Context context = getActivity();
                String msg = context.getString(R.string.msg_missing_notification_access,
                        context.getString(R.string.derived_app_name));
                return new AlertDialog.Builder(context)
                        .setTitle(R.string.title_missing_notification_access)
                        .setMessage(msg)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(R.string.title_change_settings, this)
                        .create();
            }

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ComponentName cn = new ComponentName(getActivity(), NotificationListener.class);
                Bundle showFragmentArgs = new Bundle();
                showFragmentArgs.putString(EXTRA_FRAGMENT_ARG_KEY, cn.flattenToString());

                Intent intent = null;
                if (Utilities.ATLEAST_LOLLIPOP_MR1) {
                    intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(EXTRA_FRAGMENT_ARG_KEY, cn.flattenToString())
                            .putExtra(EXTRA_SHOW_FRAGMENT_ARGS, showFragmentArgs);
                    getActivity().startActivity(intent);
                } else {
                    // TODO 5.0以前是否有消息通知
                    getActivity().startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            }
        }
    }
}
