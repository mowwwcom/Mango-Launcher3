package com.android.launcher3.util;

import android.content.Context;
import android.util.Log;

import com.android.launcher3.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author tic
 * created on 18-10-18
 */
public class DateTimes {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = 60 * 60;
    private static final int SECONDS_IN_DAY = 24 * 60 * 60;

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
    public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    /**
     * 时间格式 yyyy-MM-dd hh:mm:ss
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式 yyyy-MM-dd hh:mm:ss
     */
    public static final String DATETIME_FORMAT2 = "yyyy-MM-dd HH:mm";
    /**
     * 时间格式 yyyy/MM/dd HH:mm
     */
    public static final String DATETIME_FORMAT3 = "yyyy/MM/dd HH:mm";
    /**
     * 时间格式 yyyy-MM-dd
     */
    public static final String DATETIME_FORMAT4 = "yyyy-MM-dd";
    /**
     * 时间格式   HH:mm:ss
     */
    public static final String DATETIME_FORMAT5 = "HH:mm:ss";
    /**
     * 时间格式   HH:mm
     */
    public static final String DATETIME_FORMAT6 = "HH:mm";
    /**
     * 时间格式   yyyy年MM月dd日
     */
    public static final String DATETIME_FORMAT7 = "yyyy年MM月dd日";
    /**
     * 时间格式    yyyy.MM.dd
     */
    private static final String DATETIME_FORMAT8 = "yyyy.MM.dd";
    /**
     * 时间格式    yyyyMMdd
     */
    public static final String DATETIME_FORMAT9 = "yyyyMMdd";

    public static Date convertStringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    public static String convertDateToString(long milliseconds) {
        Date date = milliseconds2Date(milliseconds);
        return convertDateToString(date, DATETIME_FORMAT);
    }

    /**
     * 将时间转换成毫秒
     */
    public static long date2millionSeconds(String date) {
        long d = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
            Date parse = sdf.parse(date);
            if (parse != null) {
                d = parse.getTime();
            }
        } catch (ParseException e) {
            Log.i("error", e.getMessage());
        }
        return d;
    }

    /**
     * 时间毫秒数转为日期类型
     */
    public static Date milliseconds2Date(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar.getTime();
    }

    /**
     * 秒转换日期
     *
     * @param seconds
     * @return
     */
    public static Date seconds2Date(long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);
        return calendar.getTime();
    }

    public static String now() {
        return convertDateToString(new Date(), DATETIME_FORMAT);
    }

    public static String getDateTimeByFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 计算两个时间间隔是否在七天内 03. *
     *
     * @param date1       开始时间
     * @param date2       结束时间
     * @param intervalDay 开始时间与结束时间指定的间隔 *@return 如果开始时间与结束时间的日期间隔之差小于或者intervalDay
     */
    public static boolean computeTwoDaysWithInSpecified(String date1, String date2, final int intervalDay) {
        boolean isPositive = intervalDay == Math.abs(intervalDay);
        Date startDate = convertStringToDate(date1, DATETIME_FORMAT4);
        Date endDate = convertStringToDate(date2, DATETIME_FORMAT4);
        if (startDate == null || endDate == null) {
            return false; // 日期格式错误，判断不在范围内
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        int dayInterval = (int) (timeLong / 1000 / 60 / 60 / 24);
        if (isPositive) {
            return dayInterval >= 0 && dayInterval <= intervalDay;
        } else {
            return dayInterval >= intervalDay && dayInterval <= 0;
        }
    }

    /**
     * 计算是否超出间隔时间，单位秒
     */
    public static boolean offTimeInterval(Date start, Date end, int intervalSecond) {
        long div = end.getTime() - start.getTime();
        int value = (int) (div / 1000);
        return value > intervalSecond;
    }

    /**
     * 获取今天指定天数之前的日期
     */
    public static String findBeforeToday(int dayInterval) {
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_MONTH, -dayInterval);
        return convertDateToString(calendar.getTime(), DATETIME_FORMAT4);
    }

    /**
     * 获取今天指定天数之后的日期
     */
    public static String findAfterToday(int dayInterval) {
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_MONTH, +dayInterval);
        return convertDateToString(calendar.getTime(), DATETIME_FORMAT4);
    }

    public static String getNowTime(String format) {
        SimpleDateFormat date = new SimpleDateFormat(format, Locale.getDefault());
        return date.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 日期字符串转化成日期对象，根据时区
     */
    public static synchronized Date stringConvertDateTimeByZone(String string, String timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT, Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        try {
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得指定年、月、日的日期对象
     *
     * @param month 例如需要获取8月，实际传入的值是7
     */
    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * @return 获取当前所在的年份
     */
    public static int getThisYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * @return 获取当前所在的月份
     */
    public static int getThisMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH);
    }

    /**
     * @return 获取今天
     */
    public static int getThisDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取星期几
     */
    public static int getWeekDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 拼接成指定日期的开始时间
     *
     * @param date (yyyy-MM-dd)
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String pinFirstTime(String date) {
        return date + " 00:00:00";
    }

    /**
     * 拼接成指定日期的结束时间
     *
     * @param date (yyyy-MM-dd)
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String pinLastTime(String date) {
        return date + " 23:59:59";
    }

    /**
     * 指定的时间点是否在今天之后
     */
    public static boolean after(int aYear, int aMonth, int aDay) {
        Calendar a = Calendar.getInstance();
        a.set(aYear, aMonth, aDay);
        Calendar b = Calendar.getInstance();
        b.set(Calendar.HOUR_OF_DAY, 23);
        b.set(Calendar.MINUTE, 59);
        b.set(Calendar.SECOND, 59);
        return a.after(b);
    }

    /**
     * 将时间毫秒数转换成天数
     */
    public static int convertTimeMillsToDays(long timeMills) {
        if (timeMills > 0) {
            long oneDayMills = 1000 * 60 * 60 * 24;//一天的毫秒数
            return (int) ((timeMills + oneDayMills - 1) / oneDayMills);
        } else {
            return 0;
        }
    }

    /**
     * 获得本天的开始时间，即2014-08-29 00:00:00
     */
    public static Date getCurrentDayStartTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT4, Locale.getDefault());
        try {
            now = sdf.parse(sdf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 判断当前日期是星期几
     */
    private int getWeekDay(Date pTime) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(pTime);
            return c.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns elapsed time for the given millis, in the following format:
     * 2d 5h 40m 29s
     *
     * @param context     the application context
     * @param millis      the elapsed time in milli seconds
     * @param withSeconds include seconds?
     * @return the formatted elapsed time
     */
    public static String formatElapsedTime(Context context, double millis, boolean withSeconds) {
        StringBuilder sb = new StringBuilder();
        int seconds = (int) Math.floor(millis / 1000);
        if (!withSeconds) {
            // Round up.
            seconds += 30;
        }

        int days = 0, hours = 0, minutes = 0;
        if (seconds >= SECONDS_IN_DAY) {
            days = seconds / SECONDS_IN_DAY;
            seconds -= days * SECONDS_IN_DAY;
        }
        if (seconds >= SECONDS_IN_HOUR) {
            hours = seconds / SECONDS_IN_HOUR;
            seconds -= hours * SECONDS_IN_HOUR;
        }
        if (seconds >= SECONDS_IN_MINUTE) {
            minutes = seconds / SECONDS_IN_MINUTE;
            seconds -= minutes * SECONDS_IN_MINUTE;
        }
        if (withSeconds) {
            if (days > 0) {
                sb.append(context.getString(R.string.str_time_days,
                        days, hours, minutes, seconds));
            } else if (hours > 0) {
                sb.append(context.getString(R.string.str_time_hours,
                        hours, minutes, seconds));
            } else if (minutes > 0) {
                sb.append(context.getString(R.string.str_time_minutes, minutes, seconds));
            } else {
                sb.append(context.getString(R.string.str_time_seconds, seconds));
            }
        } else {
            if (days > 0) {
                sb.append(context.getString(R.string.str_time_days_no_seconds,
                        days, hours, minutes));
            } else if (hours > 0) {
                sb.append(context.getString(R.string.str_time_hours_no_seconds,
                        hours, minutes));
            } else {
                sb.append(context.getString(R.string.str_time_minutes_no_seconds, minutes));
            }
        }
        return sb.toString();
    }

    public static boolean isSameDate(Date srcDate, Date desDate) {
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(srcDate);

        Calendar preCalendar = Calendar.getInstance();
        preCalendar.setTime(desDate);

        if (preCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR)) {
            if (preCalendar.get(Calendar.DAY_OF_YEAR) == nowCalendar.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取当日00:00:00的时间戳,东八区则为早上八点
     */
    public static long getZeroClockTimestamp(long time) {
        long currentStamp = time;
        currentStamp -= currentStamp % DAY_IN_MILLIS;
        return currentStamp;
    }

}
