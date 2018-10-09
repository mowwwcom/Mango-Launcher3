package com.android.launcher3.vo;

import android.graphics.drawable.Drawable;

/**
 * 设置项
 *
 * @author tic
 * created on 18-10-9
 */
public class Setting {
    private int id;
    private Drawable icon;
    private CharSequence title;
    private String summary;

    public Setting(int itemId, CharSequence title, Drawable icon) {
        this.id = itemId;
        this.title = title;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
