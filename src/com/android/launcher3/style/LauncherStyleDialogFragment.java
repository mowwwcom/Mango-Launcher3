package com.android.launcher3.style;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.android.launcher3.R;

/**
 * @author tic
 * created on 18-11-1
 */
public class LauncherStyleDialogFragment extends PreferenceDialogFragmentCompat {

    int mClickedDialogEntryIndex;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    public LauncherStyleDialogFragment() {
    }

    public static PreferenceDialogFragmentCompat newInstance(String key) {
        LauncherStyleDialogFragment fragment = new LauncherStyleDialogFragment();
        Bundle b = new Bundle(1);
        b.putString("key", key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            LauncherStylePreference preference = this.getListPreference();
            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            }

            this.mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
            this.mEntries = preference.getEntries();
            this.mEntryValues = preference.getEntryValues();
        } else {
            this.mClickedDialogEntryIndex = savedInstanceState.getInt("ListPreferenceDialogFragment.index", 0);
            this.mEntries = savedInstanceState.getCharSequenceArray("ListPreferenceDialogFragment.entries");
            this.mEntryValues = savedInstanceState.getCharSequenceArray("ListPreferenceDialogFragment.entryValues");
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ListPreferenceDialogFragment.index", this.mClickedDialogEntryIndex);
        outState.putCharSequenceArray("ListPreferenceDialogFragment.entries", this.mEntries);
        outState.putCharSequenceArray("ListPreferenceDialogFragment.entryValues", this.mEntryValues);
    }

    private LauncherStylePreference getListPreference() {
        return (LauncherStylePreference) this.getPreference();
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_launcher_style, null);
        builder.setView(view);

        RadioGroup group = view.findViewById(R.id.radios);
        group.clearCheck();

        String value = getListPreference().getValue();
        if (Integer.valueOf(value) == LauncherStyleHandler.STYLE_DRAWER) {
            group.check(R.id.cb_drawer);
        } else {
            group.check(R.id.cb_standard);
        }
        // after set default value
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            mClickedDialogEntryIndex = checkedId == R.id.cb_drawer ?
                    LauncherStyleHandler.STYLE_DRAWER : LauncherStyleHandler.STYLE_STANDARD;
            Dialog dialog = getDialog();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                onDialogClosed(true);
            }
        });


        builder.setPositiveButton(null, null);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        LauncherStylePreference preference = this.getListPreference();
        if (positiveResult && this.mClickedDialogEntryIndex >= 0) {
            String value = this.mEntryValues[this.mClickedDialogEntryIndex].toString();
            String name = this.mEntries[this.mClickedDialogEntryIndex].toString();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
                preference.setSummary(name);
            }
        }

    }
}
