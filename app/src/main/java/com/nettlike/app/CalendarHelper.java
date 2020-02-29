package com.nettlike.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarHelper {

    public String getIntervaledMessageTime(String createdAt){
        String dateString = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        SimpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        newFormat.setTimeZone(TimeZone.getDefault());
        compareFormat.setTimeZone(TimeZone.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        Date msgDate = null;
        String currentDateString = compareFormat.format(currentDate);
        try {
            msgDate = df.parse(createdAt);
            Date date = compareFormat.parse(createdAt);
            currentDate = compareFormat.parse(currentDateString);
            String compareDateString = compareFormat.format(msgDate);
            if (date.before(currentDate))
                dateString = createdAt;
            else
                dateString = newFormat.format(msgDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public String getPostTime(String createdAt) {
        String dateString = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
//        SimpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = df.parse(createdAt);
            dateString = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newFormat.setTimeZone(TimeZone.getDefault());
        return dateString;
    }
}
