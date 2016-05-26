package com.training.startandroid.trapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.training.startandroid.trapp.database.interfaces.CursorConverter;
import com.training.startandroid.trapp.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private final static String ERROR_TAG = "Exception: ";
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, Constants.DB_SQLITE_NAME, null, Constants.DB_VERSION);
    }

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, Constants.DB_SQLITE_NAME, factory, Constants.DB_VERSION);
    }

    public void open() {
        sqLiteDatabase = getWritableDatabase();
    }

    public void close() {
        if (sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();

        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("DBHelper", "Method onCreate in DBHelper in");
        try {
            db.beginTransaction();

            db.execSQL(Constants.CREATE_SQLITE_TABLE_CATALOGS);
            db.execSQL(Constants.CREATE_SQLITE_TABLE_WORDS);
            db.execSQL(Constants.CREATE_SQLITE_TABLE_CATALOGS_AND_WORDS_RELATIONS);

            db.execSQL(Constants.CREATE_TABLE_CATALOGS_IMAGE);
            db.execSQL(Constants.CREATE_TABLE_WORDS_IMAGE);
            db.execSQL(Constants.CREATE_TABLE_WORDS_SOUND);

            db.execSQL(Constants.CREATE_SQLITE_TABLE_TRANSLATE_WORDS);

            db.execSQL(Constants.CREATE_SQLITE_TABLE_GOOGLE_TRANSLATE);
            db.execSQL(Constants.CREATE_SQLITE_TABLE_YANDEX_TRANSLATE);
            db.execSQL(Constants.CREATE_SQLITE_TABLE_CUSTOM_TRANSLATE);

            db.execSQL(Constants.CREATE_SQLITE_TRIGGER_DELETE_FREE_WORDS);

            db.execSQL(Constants.CREATE_SQLITE_TRIGGER_DELETE_FREE_GOOGLE_TRANSLATION);
            db.execSQL(Constants.CREATE_SQLITE_TRIGGER_DELETE_FREE_YANDEX_TRANSLATION);
            db.execSQL(Constants.CREATE_SQLITE_TRIGGER_DELETE_FREE_CUSTOM_TRANSLATION);

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d(ERROR_TAG, ex.toString());
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public synchronized Object executeSelectQuery(final String query, String[] parameter, CursorConverter converter) {

        Cursor cursor = null;
        try {
            /**
             *  Usually I must using method query without rawQuery
             */
            cursor = sqLiteDatabase.rawQuery(query, parameter);
        } catch (Exception ex) {
            Log.d(ERROR_TAG, ex.toString());
        } finally {
            return converter.convert(cursor);
        }


    }

    public synchronized int executeUpdateDeleteQuery(Constants.ActionStatement actionStatement, Object[][] parameters) {

        try {
            sqLiteDatabase.beginTransaction();
            SQLiteStatement preparedStatement = sqLiteDatabase.compileStatement(StatementBuilder.getStatement(actionStatement));

            int count = 0;

            for (Object[] oneRowParam : parameters) {
                preparedStatement.clearBindings();
                bindingParametersInPreparedStatement(preparedStatement, oneRowParam);
                count += preparedStatement.executeUpdateDelete();
            }

            sqLiteDatabase.setTransactionSuccessful();

            return count;

        } catch (Exception ex) {
            Log.e(ERROR_TAG, ex.toString());
            return -1;
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }

    public synchronized List<Integer> executeInsertQuery(Constants.ActionStatement actionStatement, Object[][] parameters) {

        try {
            sqLiteDatabase.beginTransaction();
            SQLiteStatement preparedStatement = sqLiteDatabase.compileStatement(StatementBuilder.getStatement(actionStatement));

            long id;
            List<Integer> listInsertId = new ArrayList<>(parameters.length);

            for (Object[] oneRowParam : parameters) {
                preparedStatement.clearBindings();
                bindingParametersInPreparedStatement(preparedStatement, oneRowParam);
                id = preparedStatement.executeInsert();
                listInsertId.add((int) id);
            }
            sqLiteDatabase.setTransactionSuccessful();

            return listInsertId;

        } catch (Exception ex) {
            Log.e(ERROR_TAG, ex.toString());
            return null;
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }

    private void bindingParametersInPreparedStatement(SQLiteStatement preparedStatement, Object[] parameters) {

        for (int i = 0; i < parameters.length; i++) {

            if (parameters[i] instanceof String) {
                preparedStatement.bindString(i + 1, (String) parameters[i]);
            } else if (parameters[i] instanceof Integer) {
                preparedStatement.bindLong(i + 1, (Integer) parameters[i]);
            }
        }
    }

}
