package com.training.startandroid.trapp.database;

import android.content.Context;

/**
 * Created by Администратор on 28.04.2016.
 */
public class DatabaseConnection {

    private static final String NULL_POINTER_EXCEPTION_MSG = "DBHelper was not initialized";
    private static DBHelper dbHelper;

    private DatabaseConnection() {
    }

    ;

    public static synchronized void initializeConnection(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
    }

    public static synchronized void openConnection() throws NullPointerException {
        if (dbHelper == null) {
            throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
        }
        dbHelper.open();
    }

    public static synchronized void closeConnection() throws NullPointerException {
        if (dbHelper == null) {
            throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
        }
    }

    public static DBHelper getInstanceDBHelper() throws NullPointerException {
        if (dbHelper == null) {
            throw new NullPointerException(NULL_POINTER_EXCEPTION_MSG);
        }
        return dbHelper;
    }
}
