package com.stych.android.util;

import com.stych.android.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static String formatDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.OUTPUT_DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public static String formatDateAppFormate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.OUTPUT_DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.OUTPUT_TIME_FORMAT);
        return sdf.format(date);
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.OUTPUT_DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    public static String formatDateTime2L(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.OUTPUT_DATE_TIME_FORMAT_2L);
        return sdf.format(date);
    }

    public static Date parseDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.INPUT_DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date parseDateOurServer(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.INPUT_DATE_TIME_FORMAT_OUR_SERVER);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }


    public static String getTimestamp(Calendar calendar) {
        return Long.toString(calendar.getTimeInMillis() / 1000);
    }
}
