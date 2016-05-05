package com.training.startandroid.trapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Администратор on 05.05.2016.
 */
public class ConvertString2Date {

    private static final String pattern = "dd.mm.yyyy hh:mm:ss";

    public static Date convert(final String parsingDate) {

        Date date;

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setTimeZone(TimeZone.getDefault());

            date = dateFormat.parse(parsingDate);
            date.setTime(date.getTime() + TimeZone.getDefault().getRawOffset());

        } catch (Exception ex) {
            date = new Date();
        }

        return date;
    }
}
