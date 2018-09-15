package org.jenkinsci.plugins.util;

import java.text.SimpleDateFormat;

public class DateUtil {

    static String yyyyMMdd = "yyyy-MM-dd";
    static String yyyyMMdd_HHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String toYYYYMMDD(java.util.Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyyMMdd);
        return date != null ? formatter.format(date) : "";
    }

    public static String toYYYYMMDD_HHmmssSSS(java.util.Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyyMMdd_HHmmssSSS);
        return date != null ? formatter.format(date) : "";
    }
}
