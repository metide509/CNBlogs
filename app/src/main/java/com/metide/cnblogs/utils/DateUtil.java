package com.metide.cnblogs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author   metide
 * Date     2017/4/22
 */

public class DateUtil {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";

    /**
     * 时间戳转date
     *
     * @param time 时间戳格式
     * @return
     * @throws Exception
     */
    public static Date toDate(long time) {
        Date date = new Date(time);
        return date;
    }

    /**
     *
     * @param date
     * @return
     */
    public static long toUnix(Date date){
        long time = date.getTime();
        return time;
    }

    /**
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static long toUnix(String date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toUnix(d);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return toString(date, DEFAULT_FORMAT);
    }

    /**
     *
     * @param date
     * @param format
     * @return
     */
    public static String toString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String toString(long date) {
        return toString(date, DEFAULT_FORMAT);
    }

    /**
     *
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static String toString(long date, String format) {
        if(date <= 0){
            return "";
        }
        Date d = toDate(date);
        return toString(d, format);
    }
}
