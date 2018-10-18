package com.android.launcher3.vo;

/**
 * 应用统计信息
 *
 * @author tic
 * created on 18-9-3
 */
public class AppUsageInfo {

    private int usedCount;
    private long usedTime;
    private String packageName;

    public AppUsageInfo(int mUsedCount, long mUsedTime, String mPackageName) {
        this.usedCount = mUsedCount;
        this.usedTime = mUsedTime;
        this.packageName = mPackageName;
    }

    public void addCount() {
        usedCount++;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        AppUsageInfo standardDetail = (AppUsageInfo) o;
        if (standardDetail.getPackageName().equals(this.packageName)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (packageName + usedTime).hashCode();
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "usedCount=" + usedCount +
                ", usedTime=" + usedTime +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
