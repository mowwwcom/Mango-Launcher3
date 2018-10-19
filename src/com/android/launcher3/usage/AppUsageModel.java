package com.android.launcher3.usage;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import com.android.launcher3.BuildConfig;
import com.android.launcher3.util.DateTimes;
import com.android.launcher3.vo.AppUsageEvents;
import com.android.launcher3.vo.AppUsageInfo;
import com.google.common.base.Preconditions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * 统计手机应用的使用时长
 * 用于计算手机时长
 *
 * @author tic
 * created on 18-10-16
 */
public class AppUsageModel {

    public static final String TAG = "SoftUsageTask";
    /***
     * event source
     */
    private ArrayList<UsageEvents.Event> mEventList;
    private ArrayList<UsageStats> mStatsList;
    /**
     * event checked
     */
    private ArrayList<UsageEvents.Event> mEventListChecked;

    private ArrayList<AppUsageEvents> mUsageEvents = new ArrayList<>();
    private ArrayList<AppUsageInfo> mAppUsageInfoList = new ArrayList<>();

    private static final boolean DEBUG = false;

    /**
     * 主要的数据获取函数
     *
     * @param dayNumber 查询若干天前的数据
     * @return int        0 : event usage 均查询到了
     * 1 : event 未查询到 usage 查询到了
     * 2 : event usage 均未查询到
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int refreshData(Context context, int dayNumber) {
        mEventList = getEventList(context, dayNumber);
        mStatsList = getUsageList(context, dayNumber);

        if (mEventList == null || mEventList.size() == 0) {
            Log.i(TAG, " UseTimeDataManager-refreshData()   未查到events");

            if (mStatsList == null || mStatsList.size() == 0) {
                if (DEBUG) {
                    Log.i(TAG, " UseTimeDataManager-refreshData()   未查到stats");
                }
                return 2;
            }

            return 1;
        }

        //获取数据之后，进行数据的处理
        mEventListChecked = getEventListChecked(UsageEvents.Event.MOVE_TO_FOREGROUND
                | UsageEvents.Event.MOVE_TO_BACKGROUND);
        // refreshOneTimeDetailList(0);
        refreshPackageInfoList();
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void refreshPackageInfoList() {
        mAppUsageInfoList.clear();
        for (int i = 0; i < mStatsList.size(); i++) {
            UsageStats stats = mStatsList.get(i);
            String pkg = stats.getPackageName();
            AppUsageInfo info = new AppUsageInfo(0, stats.getTotalTimeInForeground(), pkg);
//            PackageInfo info = new PackageInfo(0, calculateUseTime(pkg), pkg);
            mAppUsageInfoList.add(info);
        }
        // update count, TODO 现不需要记录次数
//        for (int n = 0; n < mAppUsageInfoList.size(); n++) {
//            String pkg = mAppUsageInfoList.get(n).getPackageName();
//            for (int m = 0; m < mUsageEvents.size(); m++) {
//                if (pkg.equals(mUsageEvents.get(m).getPkgName())) {
//                    mAppUsageInfoList.get(n).addCount();
//                }
//            }
//        }
    }

    /**
     * 获取手机使用时间
     *
     * @param context
     */
    public long getAppUseTime(Context context) {
        Preconditions.checkNotNull(mAppUsageInfoList);
        long time = 0;
        for (AppUsageInfo pi : mAppUsageInfoList) {
            if (pi.getPackageName().equals(BuildConfig.APPLICATION_ID)) {
                // ignore myself, 保活机制会使我们的应用一直在前台进程
                continue;
            }
            time += pi.getUsedTime();
            if (pi.getUsedTime() != 0) {
                Log.i(TAG, pi.getPackageName()
                        + " ==> " + DateTimes.formatElapsedTime(context, pi.getUsedTime(), true));
            }
        }
        return time;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageEvents.Event> getEventList(Context context, int dayNumber) {
        long endTime, startTime;
        if (dayNumber == 0) {
            endTime = System.currentTimeMillis();
            startTime = DateTimes.getZeroClockTimestamp(endTime);
        } else {
            endTime = DateTimes.getZeroClockTimestamp(System.currentTimeMillis() - (dayNumber - 1) * DateTimes.DAY_IN_MILLIS) - 1;
            startTime = endTime - DateTimes.DAY_IN_MILLIS + 1;
        }
        return getEventList(context, startTime, endTime);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageStats> getUsageList(Context context, int dayNumber) {
        long endTime, startTime;
        if (dayNumber == 0) {
            endTime = System.currentTimeMillis();
            startTime = DateTimes.getZeroClockTimestamp(endTime);
        } else {
            endTime = DateTimes.getZeroClockTimestamp(System.currentTimeMillis() - (dayNumber - 1) * DateTimes.DAY_IN_MILLIS) - 1;
            startTime = endTime - DateTimes.DAY_IN_MILLIS + 1;
        }

        return getUsageList(context, startTime, endTime);
    }

    /**
     * 仅保留 event 中 type 为 1或者2 的
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageEvents.Event> getEventListChecked(int eventType) {
        ArrayList<UsageEvents.Event> list = new ArrayList<>();
        for (int i = 0; i < mEventList.size(); i++) {
            int event = mEventList.get(i).getEventType();
            if ((event & eventType) > 0) {
                list.add(mEventList.get(i));
            }
        }
        return list;
    }

    /**
     * TODO 该方法时间计算偏短，需优化
     * <p>
     * 从 startIndex 开始分类event  直至将event分完
     * 每次从0开始，将原本的 mUsageEvents 清除一次,然后开始分类
     */
    @Deprecated
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void refreshOneTimeDetailList(int startIndex) {
        if (DEBUG) {
            Log.i(TAG, "  refreshOneTimeDetailList()     startIndex : " + startIndex);
        }

        if (startIndex == 0) {
            if (DEBUG) {
                Log.i(TAG, "  refreshOneTimeDetailList()     每次从0开始，将原本的 mUsageEvents 清除一次,然后开始分类 ");
            }
            if (mUsageEvents != null) {
                mUsageEvents.clear();
            }
        }

        long totalTime = 0;
        int usedIndex = 0;
        String pkg = null;
        ArrayList<UsageEvents.Event> list = new ArrayList();
        for (int i = startIndex; i < mEventListChecked.size(); i++) {
            if (i == startIndex) {
                if (mEventListChecked.get(i).getEventType() == 2) {
                    if (DEBUG) {
                        Log.i(TAG, "  refreshOneTimeDetailList()     warning : 每次打开一个app  第一个activity的类型是 2     ");
                    }
                }
                pkg = mEventListChecked.get(i).getPackageName();
                list.add(mEventListChecked.get(i));
            } else {
                if (pkg != null) {
                    if (pkg.equals(mEventListChecked.get(i).getPackageName())) {
                        list.add(mEventListChecked.get(i));
                        if (i == mEventListChecked.size() - 1) {
                            usedIndex = i;
                        }
                    } else {
                        usedIndex = i;
                        break;
                    }
                }
            }
        }

        if (DEBUG) {
            Log.i(TAG, "   mEventListChecked 分类:   before  check :   list.size() = " + list.size());
        }
        checkEventList(list);
        if (DEBUG) {
            Log.i(TAG, "   mEventListChecked 分类:   after  check :   list.size() = " + list.size());
            Log.i(TAG, "   mEventListChecked 分类:  本次启动的包名：" + list.get(0).getPackageName() + "   时间：" + DateUtils
                    .formatSameDayTime(list.get(0).getTimeStamp(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));
        }
        final int ahead = 2;
        for (int i = 1; i < list.size(); i += ahead) {
            if (list.get(i).getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND
                    && list.get(i - 1).getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                totalTime += (list.get(i).getTimeStamp() - list.get(i - 1).getTimeStamp());
            }
        }
        AppUsageEvents events = new AppUsageEvents(pkg, totalTime, list);
        mUsageEvents.add(events);

        if (usedIndex < mEventListChecked.size() - 1) {
            refreshOneTimeDetailList(usedIndex);
        } else {
            if (DEBUG) {
                Log.i(TAG, "  refreshOneTimeDetailList()     已经将  mEventListChecked 分类完毕   ");
            }
        }

    }

    /**
     * 采用回溯的思想：
     * 从头遍历EventList，如果发现异常数据，则删除该异常数据，并从头开始再次进行遍历，直至无异常数据
     * （异常数据是指：event 均为 type=1 和type=2 ，成对出现，一旦发现未成对出现的数据，即视为异常数据）
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void checkEventList(ArrayList<UsageEvents.Event> list) {
        boolean isCheckAgain = false;
        final int aHead = 2;
        for (int i = 0; i < list.size() - 1; i += aHead) {
            if (list.get(i).getClassName().equals(list.get(i + 1).getClassName())) {
                if (list.get(i).getEventType() != 1) {
                    if (DEBUG) {
                        Log.i(TAG, "   EventList 出错  ： " + list.get(i).getPackageName() + "  " + DateUtils.formatSameDayTime(list.get(i).getTimeStamp(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM).toString());
                    }
                    list.remove(i);
                    isCheckAgain = true;
                    break;
                }
                if (list.get(i + 1).getEventType() != 2) {
                    if (DEBUG) {
                        Log.i(TAG, "   EventList 出错 ： " + list.get(i + 1).getPackageName() + "  " + DateUtils.formatSameDayTime(list.get(i + 1).getTimeStamp(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM).toString());
                    }
                    list.remove(i);
                    isCheckAgain = true;
                    break;
                }
            } else {
                //i和i+1的className对不上，则删除第i个数据，重新检查
                list.remove(i);
                isCheckAgain = true;
                break;
            }
        }
        if (isCheckAgain) {
            checkEventList(list);
        }
    }

    private long calculateUseTime(String pkg) {
        long useTime = 0;
        for (int i = 0; i < mUsageEvents.size(); i++) {
            if (mUsageEvents.get(i).getPkgName().equals(pkg)) {
                useTime += mUsageEvents.get(i).getUseTime();
            }
        }
        if (DEBUG) {
            Log.i(TAG, "  calculateUseTime : " + useTime);
        }
        return useTime;
    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageEvents.Event> getEventList(Context context, long startTime, long endTime) {
        ArrayList<UsageEvents.Event> mEventList = new ArrayList<>();

        if (DEBUG) {
            Log.i(TAG, " EventUtils-getEventList()   Range start:" + startTime);
            Log.i(TAG, " EventUtils-getEventList()   Range end:" + endTime);
            Log.i(TAG, " EventUtils-getEventList()   Range start:" + DateTimes.convertDateToString(startTime));
            Log.i(TAG, " EventUtils-getEventList()   Range end:" + DateTimes.convertDateToString(endTime));
        }

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        if (mUsmManager == null) {
            return mEventList;
        }
        UsageEvents events = mUsmManager.queryEvents(startTime, endTime);
        while (events.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            events.getNextEvent(e);
            if (e.getEventType() == 1 || e.getEventType() == 2) {
                if (DEBUG) {
                    Log.i(TAG, " EventUtils-getEventList()  " + e.getTimeStamp() + "   event:" + e.getClassName() + "   type = " + e.getEventType());
                }
                mEventList.add(e);
            }
        }

        return mEventList;
    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ArrayList<UsageStats> getUsageList(Context context, long startTime, long endTime) {
        if (DEBUG) {
            Log.i(TAG, " EventUtils-getUsageList()   Range start:" + startTime);
            Log.i(TAG, " EventUtils-getUsageList()   Range end:" + endTime);
            Log.i(TAG, " EventUtils-getUsageList()   Range start:"
                    + DateTimes.convertDateToString(startTime));
            Log.i(TAG, " EventUtils-getUsageList()   Range end:" + DateTimes.convertDateToString(endTime));
        }

        ArrayList<UsageStats> list = new ArrayList<>();

        UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService("usagestats");
        if (mUsmManager == null) {
            return list;
        }
        Map<String, UsageStats> map = mUsmManager.queryAndAggregateUsageStats(startTime, endTime);
        for (Map.Entry<String, UsageStats> entry : map.entrySet()) {
            UsageStats stats = entry.getValue();
            if (stats.getTotalTimeInForeground() > 0) {
                list.add(stats);
                if (DEBUG) {
                    Log.i(TAG, " EventUtils-getUsageList()   stats:" + stats.getPackageName()
                            + "   TotalTimeInForeground = " + DateTimes.formatElapsedTime(context, stats.getTotalTimeInForeground(), true));
                }
            }
        }
        return list;
    }
}
