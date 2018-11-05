package com.android.launcher3.ui;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.launcher3.R;
import com.android.launcher3.util.security.PermissionManager;

/**
 * @author tic
 *         created on 18-9-17
 */
public abstract class BaseCompatActivity extends AppCompatActivity implements PermissionManager.Callback {

    private String TAG = getClass().getSimpleName();

    private ViewGroup mContent;
    private PermissionManager mSecurity;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContent = findViewById(R.id.main_content);
        mToolbar = findViewById(R.id.toolbar);

        if (mToolbar == null) {
            Log.i(TAG, "No actionBar");
        } else {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
        }

        View view = loadLayout(mContent);
        if (view != null) {
            mContent.removeAllViews();
            mContent.addView(view);
        }

        mSecurity = new PermissionManager(this);
        mSecurity.requestPermission();
    }

    protected void hideToolbar(boolean hide) {
        if (mToolbar != null) {
            mToolbar.setVisibility(hide ? View.GONE : View.VISIBLE);
        }
    }

    protected void showHomeButton() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar == null) {
            Log.i(TAG, "No actionBar found");
        } else {
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setHomeButtonEnabled(true);
        }
    }

    protected void showBackButton() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar == null) {
            Log.i(TAG, "No actionBar found");
        } else {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeButtonEnabled(true);
        }
    }


    @Override
    public void setTitle(CharSequence charSequence) {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle(charSequence);
        } else {
            super.setTitle(charSequence);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    private View loadLayout(ViewGroup group) {
        int layout = layout();
        if (layout == 0) {
            return null;
        } else {
            return LayoutInflater.from(this).inflate(layout, group, false);
        }
    }

    protected abstract int layout();

    protected void addView(View view, int index) {
        mContent.addView(view, index);
    }

    protected void addView(View view) {
        mContent.removeAllViews();
        mContent.addView(view);
    }

    protected void replace(Fragment fragment) {
        mContent.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }

    @Override
    public void onPermissionRefuse(@NonNull String permissions) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
