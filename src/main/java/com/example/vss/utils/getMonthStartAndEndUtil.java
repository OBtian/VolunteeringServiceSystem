package com.example.vss.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class getMonthStartAndEndUtil {
    /**
    * 设置月份
    */
    public static Calendar getDate(int month_num){
        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        int setYear, setMonth, currentYear, currentMonth;

        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);

        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        if (month_num > currentMonth ){
            setYear = currentYear - (month_num - currentMonth) / 12 + 1;
            setMonth = 12 - (month_num - currentMonth) % 12;
        }else{
            setYear = currentYear;
            setMonth = currentMonth - month_num;
        }

        calendar.set(Calendar.YEAR,setYear);
        calendar.set(Calendar.MONTH,setMonth);

        return calendar;
    }
    /**
     * 获取当月开始时间和结束时间
     *
     * @return
     */
    public static Map getMonthTime(int month_num) {
        Calendar calendar = getDate(month_num);
        Long startTime = getMonthStartTime(calendar);
        Long endTime = getMonthEndTime(calendar);
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;
    }

    /**
     * 获取当月开始时间
     */
    public static Long getMonthStartTime(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();

    }

    /**
     * 获取当月的结束时间戳
     */
    public static Long getMonthEndTime(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
}
