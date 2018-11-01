package com.android.launcher3.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;
import android.util.AttributeSet;
import android.util.TypedValue;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author tic
 * created on 18-11-1
 */
public class TypeArrays {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

    public static boolean hasAttribute(@NonNull XmlPullParser parser, @NonNull String attrName) {
        return parser.getAttributeValue("http://schemas.android.com/apk/res/android", attrName) != null;
    }

    public static float getNamedFloat(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, @StyleableRes int resId, float defaultValue) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? defaultValue : a.getFloat(resId, defaultValue);
    }

    public static boolean getNamedBoolean(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, @StyleableRes int resId, boolean defaultValue) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? defaultValue : a.getBoolean(resId, defaultValue);
    }

    public static int getNamedInt(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, @StyleableRes int resId, int defaultValue) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? defaultValue : a.getInt(resId, defaultValue);
    }

    @ColorInt
    public static int getNamedColor(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, @StyleableRes int resId, @ColorInt int defaultValue) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? defaultValue : a.getColor(resId, defaultValue);
    }

    @AnyRes
    public static int getNamedResourceId(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, @StyleableRes int resId, @AnyRes int defaultValue) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? defaultValue : a.getResourceId(resId, defaultValue);
    }

    @Nullable
    public static String getNamedString(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, @StyleableRes int resId) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? null : a.getString(resId);
    }

    @Nullable
    public static TypedValue peekNamedValue(@NonNull TypedArray a, @NonNull XmlPullParser parser, @NonNull String attrName, int resId) {
        boolean hasAttr = hasAttribute(parser, attrName);
        return !hasAttr ? null : a.peekValue(resId);
    }

    @NonNull
    public static TypedArray obtainAttributes(@NonNull Resources res, @Nullable Resources.Theme theme, @NonNull AttributeSet set, @NonNull int[] attrs) {
        return theme == null ? res.obtainAttributes(set, attrs) : theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    public static boolean getBoolean(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex, boolean defaultValue) {
        boolean val = a.getBoolean(fallbackIndex, defaultValue);
        return a.getBoolean(index, val);
    }

    @Nullable
    public static Drawable getDrawable(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        Drawable val = a.getDrawable(index);
        if (val == null) {
            val = a.getDrawable(fallbackIndex);
        }

        return val;
    }

    public static int getInt(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex, int defaultValue) {
        int val = a.getInt(fallbackIndex, defaultValue);
        return a.getInt(index, val);
    }

    @AnyRes
    public static int getResourceId(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex, @AnyRes int defaultValue) {
        int val = a.getResourceId(fallbackIndex, defaultValue);
        return a.getResourceId(index, val);
    }

    @Nullable
    public static String getString(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        String val = a.getString(index);
        if (val == null) {
            val = a.getString(fallbackIndex);
        }

        return val;
    }

    @Nullable
    public static CharSequence getText(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        CharSequence val = a.getText(index);
        if (val == null) {
            val = a.getText(fallbackIndex);
        }

        return val;
    }

    @Nullable
    public static CharSequence[] getTextArray(@NonNull TypedArray a, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        CharSequence[] val = a.getTextArray(index);
        if (val == null) {
            val = a.getTextArray(fallbackIndex);
        }

        return val;
    }

    public static int getAttr(@NonNull Context context, int attr, int fallbackAttr) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        return value.resourceId != 0 ? attr : fallbackAttr;
    }

    private TypeArrays() {
    }
}
