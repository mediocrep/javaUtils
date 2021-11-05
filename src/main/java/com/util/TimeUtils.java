package com.util;

import com.util.constants.Constants;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static DateTimeFormatter dateTimeFormatter;

    public static String getTimeStr(LocalDateTime localDateTime, String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
//        LocalDateTime localDateTime = LocalDateTime.now(); "YYYY-MM-dd HH:mm:ss"
        return dateTimeFormatter.format(localDateTime);
    }

    public static String getTimeStr(LocalDateTime localDateTime) {
        return getTimeStr(localDateTime, Constants.TIME_YMDHMS);
    }

    public static String getTimeStr(String pattern) {
        return getTimeStr(LocalDateTime.now(), pattern);
    }

    public static String getTimeStr() {
        return getTimeStr(LocalDateTime.now());
    }

    public static Long getTime(LocalDateTime localDateTime, String timeType) {
        if (Constants.TIME_SECOND.equals(timeType)) {
            return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        }else if (Constants.TIME_MILLISECOND.equals(timeType)) {
            return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        }
        return null;
    }

    public static Long getTime(LocalDateTime localDateTime) {
        return getTime(localDateTime, Constants.TIME_MILLISECOND);
    }

    public static Long getTime(String timeType) {
        return getTime(LocalDateTime.now(), timeType);
    }

    public static Long getTime() {
        return getTime(LocalDateTime.now());
    }

}
