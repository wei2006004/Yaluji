package com.ylj.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class DateUtils {

    public static String timeToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");
        return sdf.format(date);
    }

    public static Date stringToTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String sqlDateToString(java.sql.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static java.sql.Date stringToSqlDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new java.sql.Date(sdf.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(new Date().getTime());
    }

    public static String timeDiffText(long time) {
        long timeMao = time / 1000;
        long hour = timeMao / 3600;
        long minute = timeMao / 60;
        long second = timeMao % 60;
        return String.format("%2d:%2d:%2d",hour,minute,second);
    }
}
