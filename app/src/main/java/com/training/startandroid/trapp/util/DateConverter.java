package com.training.startandroid.trapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateConverter {

    private static final String patternInputDate = "dd.mm.yyyy hh:mm:ss";
    private static final String patternOutputDate = "HH:mm dd/MM/yy";

    public static Date convertString2Date(final String parsingDate) {

        Date date;

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(patternInputDate);
            dateFormat.setTimeZone(TimeZone.getDefault());

            date = dateFormat.parse(parsingDate);
            date.setTime(date.getTime() + TimeZone.getDefault().getRawOffset());

        } catch (Exception ex) {
            date = new Date();
        }

        return date;
    }

    public static String convertDate2String(final Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(patternOutputDate);
        return dateFormat.format(date);
    }
}
