package com.training.startandroid.trapp.util;


import android.content.Context;
import android.content.res.Configuration;

public class RecyclerViewConfiguration {

    private static boolean isTablet(Context context) {

        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xlarge || large);
    }

    public static int getDisplayColumns(Context context) {

        int columnCount = 1;
        if (isTablet(context)) {
            columnCount = 2;
        }

        return columnCount;
    }
}
