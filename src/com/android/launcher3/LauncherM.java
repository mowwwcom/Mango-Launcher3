package com.android.launcher3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.android.launcher3.dashboard.SmartAssistantModel;
import com.android.launcher3.dashboard.SmartAssistants;
import com.android.launcher3.model.LocationModel;
import com.android.launcher3.test.DumpReceiver;
import com.android.launcher3.util.Broadcasts;
import com.android.launcher3.util.security.Security;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author tic
 *         created on 18-10-9
 */
@Security({
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,

})
public class LauncherM extends Launcher {


    private SmartAssistantModel mSmartAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLauncherCallbacks(callbacks);
        super.onCreate(savedInstanceState);

        mSmartAssistant = new SmartAssistantModel();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SmartAssistants.ACTION_ENABLE_SMART_ASSISTANT);
        Broadcasts.registerLocal(this, mLocalReceiver, filter);

        IntentFilter dumpFilter = new IntentFilter();
        dumpFilter.addAction(DumpReceiver.ACTION_DUMP_DB);

        registerReceiver(mDumpReceiver, dumpFilter);
    }

    BroadcastReceiver mDumpReceiver = new DumpReceiver();

    private BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;
            switch (action) {
                case SmartAssistants.ACTION_ENABLE_SMART_ASSISTANT:
                    callbacks.populateCustomContentContainer();
                    break;
                default:
                    break;
            }
        }
    };

    private LocationModel mLocationModel;

    private LauncherCallbacks callbacks = new LauncherCallbacks() {
        @Override
        public void onCreate(Bundle savedInstanceState) {
//            LauncherAppState app = LauncherAppState.getInstance(LauncherM.this);
//            mLocationModel = app.getLocationModel();
//            mLocationModel.initLocation(LauncherM.this);
        }

        @Override
        public void onResume() {
//            mLocationModel.startLocation();
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
            Broadcasts.unRegisterLocal(LauncherM.this, mLocalReceiver);
            unregisterReceiver(mDumpReceiver);
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
            return true;
        }

        @Override
        public boolean hasCustomContentToLeft() {
            return SmartAssistants.isEnabled(LauncherM.this);
        }

        @Override
        public void populateCustomContentContainer() {
            if (mSmartAssistant == null) {
                mSmartAssistant = new SmartAssistantModel();
            }

            if (SmartAssistants.isEnabled(LauncherM.this)) {
                View customContent = getLayoutInflater()
                        .inflate(R.layout.view_dashboard, getDragLayer(), false);

                mWorkspace.createCustomContentContainer();
                mWorkspace.addToCustomContentPage(customContent,
                        mSmartAssistant.getCustomCallback(),
                        "dash-board");
            } else {
                mWorkspace.removeCustomContentPage();
            }
        }
    };
}
