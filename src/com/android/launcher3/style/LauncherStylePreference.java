package com.android.launcher3.style;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v7.preference.DialogPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.android.launcher3.util.TypeArrays;

/**
 * @author tic
 * created on 18-10-11
 */
public class LauncherStylePreference extends DialogPreference {

    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
    private String mValue;
    private String mSummary;
    private boolean mValueSet;

    public LauncherStylePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.v7.preference.R.styleable.ListPreference, defStyleAttr, defStyleRes);
        mEntries = TypeArrays.getTextArray(a,
                android.support.v7.preference.R.styleable.ListPreference_entries,
                android.support.v7.preference.R.styleable.ListPreference_android_entries);
        mEntryValues = TypeArrays.getTextArray(a,
                android.support.v7.preference.R.styleable.ListPreference_entryValues,
                android.support.v7.preference.R.styleable.ListPreference_android_entryValues);

        a.recycle();
        a = context.obtainStyledAttributes(attrs,
                android.support.v7.preference.R.styleable.Preference, defStyleAttr, defStyleRes);

        mSummary = TypeArrays.getString(a,
                android.support.v7.preference.R.styleable.Preference_summary,
                android.support.v7.preference.R.styleable.Preference_android_summary);
        a.recycle();
    }

    public LauncherStylePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LauncherStylePreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypeArrays.getAttr(context, android.support.v7.preference.R.attr.dialogPreferenceStyle, 16842897));
    }

    public LauncherStylePreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setEntries(CharSequence[] entries) {
        this.mEntries = entries;
    }

    public void setEntries(@ArrayRes int entriesResId) {
        this.setEntries(this.getContext().getResources().getTextArray(entriesResId));
    }

    public CharSequence[] getEntries() {
        return this.mEntries;
    }

    public void setEntryValues(CharSequence[] entryValues) {
        this.mEntryValues = entryValues;
    }

    public void setEntryValues(@ArrayRes int entryValuesResId) {
        this.setEntryValues(this.getContext().getResources().getTextArray(entryValuesResId));
    }

    public CharSequence[] getEntryValues() {
        return this.mEntryValues;
    }

    public void setValue(String value) {
        boolean changed = !TextUtils.equals(this.mValue, value);
        if (changed || !this.mValueSet) {
            this.mValue = value;
            this.mValueSet = true;
            this.persistString(value);
            if (changed) {
                this.notifyChanged();
            }
        }

    }

    @Override
    public CharSequence getSummary() {
        CharSequence entry = this.getEntry();
        return this.mSummary == null ? super.getSummary() : String.format(this.mSummary, entry == null ? "" : entry);
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        if (summary == null && this.mSummary != null) {
            this.mSummary = null;
        } else if (summary != null && !summary.equals(this.mSummary)) {
            this.mSummary = summary.toString();
        }

    }

    public void setValueIndex(int index) {
        if (this.mEntryValues != null) {
            this.setValue(this.mEntryValues[index].toString());
        }

    }

    public String getValue() {
        return this.mValue;
    }

    public CharSequence getEntry() {
        int index = this.getValueIndex();
        return index >= 0 && this.mEntries != null ? this.mEntries[index] : null;
    }

    public int findIndexOfValue(String value) {
        if (value != null && this.mEntryValues != null) {
            for (int i = this.mEntryValues.length - 1; i >= 0; --i) {
                if (this.mEntryValues[i].equals(value)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private int getValueIndex() {
        return this.findIndexOfValue(this.mValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        this.setValue(this.getPersistedString((String) defaultValue));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (this.isPersistent()) {
            return superState;
        } else {
            SavedState myState = new SavedState(superState);
            myState.value = this.getValue();
            return myState;
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state != null && state.getClass().equals(SavedState.class)) {
            SavedState myState = (SavedState) state;
            super.onRestoreInstanceState(myState.getSuperState());
            this.setValue(myState.value);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private static class SavedState extends BaseSavedState {
        String value;
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcel source) {
            super(source);
            this.value = source.readString();
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.value);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
