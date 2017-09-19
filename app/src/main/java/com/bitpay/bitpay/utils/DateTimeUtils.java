package com.bitpay.bitpay.utils;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Codeslay-03 on 3/4/2017.
 */

public class DateTimeUtils {

    public static String getDate(String sTime) {
        String date = "";
        try {
            long timestamp = Long.valueOf(sTime);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            date = DateFormat.format("dd/MM/yyyy", cal).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());

            if (date != null && date.equalsIgnoreCase(currentDate)) {
                date = "Today";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTime(String sTime) {
        String time = "";
        try {
            long timestamp = Long.valueOf(sTime);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            time = DateFormat.format("hh:mm a", cal).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getDateByTimestamp(String sTime) {
        String date = "";
        try {
            long timestamp = Long.valueOf(sTime)*1000;
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            date = DateFormat.format("dd/MM/yyyy", cal).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new Date());

            if (date != null && date.equalsIgnoreCase(currentDate)) {
                date = "Today";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTimeByTimestamp(String sTime) {
        String time = "";
        try {
            long timestamp = Long.valueOf(sTime)*1000;
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            time = DateFormat.format("hh:mm a", cal).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

}
