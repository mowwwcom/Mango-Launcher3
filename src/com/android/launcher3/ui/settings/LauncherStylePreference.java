package com.android.launcher3.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import com.android.launcher3.R;
import com.android.launcher3.style.LauncherStyleHandler;

/**
 * TODO 适配屏幕旋转变化
 *
 * @author tic
 * created on 18-10-11
 */
public class LauncherStylePreference extends DialogPreference {

    private int mChoice;
    private int mCurrentValue;

    public LauncherStylePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_launcher_style);
        setDialogIcon(null);
    }

    @Override
    protected View onCreateDialogView() {
        View view = super.onCreateDialogView();
        RadioGroup group = view.findViewById(R.id.radios);
        group.clearCheck();

        int value = getPersistedInt(LauncherStyleHandler.STYLE_DRAWER);
        if (value == LauncherStyleHandler.STYLE_DRAWER) {
            group.check(R.id.cb_drawer);
        } else {
            group.check(R.id.cb_standard);
        }
        // after set default value
        group.setOnCheckedChangeListener((group1, checkedId) -> {
            mChoice = checkedId == R.id.cb_drawer ?
                    LauncherStyleHandler.STYLE_DRAWER : LauncherStyleHandler.STYLE_STANDARD;
            dismiss();
        });
        return view;
    }

    private void dismiss() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setPositiveButton(null, null);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mChoice = mCurrentValue = getPersistedInt(LauncherStyleHandler.STYLE_DRAWER);
            setSummary((mCurrentValue == LauncherStyleHandler.STYLE_DRAWER) ?
                    R.string.launcher_style_drawer : R.string.launcher_style_standard);

        } else {
            // Set default state from the XML attribute
            mChoice = mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
            setSummary(R.string.launcher_style_drawer);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, LauncherStyleHandler.STYLE_DRAWER);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        persistInt(mChoice);
        updateSummary(mChoice);
    }

    private void updateSummary(int choice) {
        setSummary((choice == LauncherStyleHandler.STYLE_DRAWER) ?
                R.string.launcher_style_drawer : R.string.launcher_style_standard);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = mChoice;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        // mNumberPicker.setValue(myState.value);
    }

    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        // Change this data type to match the type saved by your Preference
        int value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = source.readInt();  // Change this to read the appropriate data type
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeInt(value);  // Change this to write the appropriate data type
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
