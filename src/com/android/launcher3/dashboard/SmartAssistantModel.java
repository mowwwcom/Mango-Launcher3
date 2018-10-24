package com.android.launcher3.dashboard;

import com.android.launcher3.Launcher;

/**
 * @author tic
 * created on 18-10-24
 */
public class SmartAssistantModel {

    private Launcher.CustomContentCallbacks mCustomCallbacks = new Launcher.CustomContentCallbacks() {
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
    };


    public Launcher.CustomContentCallbacks getCustomCallback() {
        return mCustomCallbacks;
    }
}
