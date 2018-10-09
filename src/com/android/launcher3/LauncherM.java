package com.android.launcher3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author tic
 * created on 18-10-9
 */
public class LauncherM extends Launcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLauncherCallbacks(callbacks);
        super.onCreate(savedInstanceState);
    }

    private LauncherCallbacks callbacks = new LauncherCallbacks() {
        @Override
        public void onCreate(Bundle savedInstanceState) {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        }

        @Override
        public void onAttachedToWindow() {

        }

        @Override
        public void onDetachedFromWindow() {

        }

        @Override
        public void dump(String prefix, FileDescriptor fd, PrintWriter w, String[] args) {

        }

        @Override
        public void onHomeIntent(boolean internalStateHandled) {

        }

        @Override
        public boolean handleBackPressed() {
            return false;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onLauncherProviderChange() {

        }

        @Override
        public void bindAllApplications(ArrayList<AppInfo> apps) {

        }

        @Override
        public boolean startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData) {
            return false;
        }

        @Override
        public boolean hasSettings() {
            return false;
        }

        @Override
        public boolean hasCustomContentToLeft() {
            return true;
        }

        @Override
        public void populateCustomContentContainer() {
            View customContent = getLayoutInflater().inflate(R.layout.view_dashboard, getDragLayer(), false);
            mWorkspace.addToCustomContentPage(customContent, new CustomContentCallbacks() {
                @Override
                public void onShow(boolean fromResume) {
                }

                @Override
                public void onHide() {
                }

                @Override
                public void onScrollProgressChanged(float progress) {

                }

                @Override
                public boolean isScrollingAllowed() {
                    return false;
                }
            }, "dash-board");
        }
    };
}
