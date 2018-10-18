package com.android.launcher3.vo;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.os.Build;

import com.android.launcher3.util.DateTimes;

import java.util.ArrayList;

/**
 * 单次打开应用详情记录
 *
 * @author tic
 * created on 18-9-3
 */
public class AppUsageEvents {

    private long useTime;
    private String pkgName;
    private ArrayList<UsageEvents.Event> events;

    public AppUsageEvents(String pkg, long useTime, ArrayList<UsageEvents.Event> events) {
        this.pkgName = pkg;
        this.useTime = useTime;
        this.events = events;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public ArrayList<UsageEvents.Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<UsageEvents.Event> events) {
        this.events = events;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getStartTime() {
        String startTime = null;
        if (events.size() > 0) {
            startTime = DateTimes.convertDateToString(events.get(0).getTimeStamp());
        }
        return startTime;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getStopTime() {
        String stopTime = null;
        if (events.size() > 0) {
            stopTime =
                    DateTimes.convertDateToString(events.get(events.size() - 1).getTimeStamp());
        }
        return stopTime;
    }

}
