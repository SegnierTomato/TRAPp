package com.training.startandroid.trapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.training.startandroid.trapp.util.Constants;


/**
 * Created by Администратор on 13.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private final static String ERROR_TAG = "Exception: ";
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, Constants.DB_SQLITE_NAME, null, Constants.DB_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

        db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void executeSelectQuery() {

        Cursor cursor = sqLiteDatabase.query(Constants.SQLITE_SELECT_QUERY_FROM_TABLE_CATALOGS);

    }

    public int executeUpdateDeleteQuery(Constants.ActionStatement actionStatement, Object[] parameters) {

        try {
            sqLiteDatabase.beginTransaction();
            SQLiteStatement preparedStatement = sqLiteDatabase.compileStatement(StatementBuilder.getStatement(actionStatement));

            for (int i=0;i<parameters.length;i++) {
                if(parameters[i] instanceof String){
                    preparedStatement.bindString(i+1, (String) parameters[i]);
                }
                else if(parameters[i] instanceof Integer){
                    preparedStatement.bindLong(i+1, (Integer)parameters[i]);
                }
            }

            int count = preparedStatement.executeUpdateDelete();
            sqLiteDatabase.setTransactionSuccessful();

            return count;

        } catch (Exception ex) {
            Log.e(ERROR_TAG, ex.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return -1;
    }

    public int executeInsertQuery(Constants.ActionStatement actionStatement, Object[] parameters) {

        try {
            sqLiteDatabase.beginTransaction();

            SQLiteStatement preparedStatement = sqLiteDatabase.compileStatement(StatementBuilder.getStatement(actionStatement));
            long id = preparedStatement.executeInsert();
            sqLiteDatabase.setTransactionSuccessful();

            return (int) id;

        } catch (Exception ex) {
            Log.e(ERROR_TAG, ex.toString());
        } finally {
            sqLiteDatabase.endTransaction();
        }
        return -1;
    }

}
