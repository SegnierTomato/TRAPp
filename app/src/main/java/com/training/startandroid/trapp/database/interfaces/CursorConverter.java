package com.training.startandroid.trapp.database.interfaces;

import android.database.Cursor;

/**
 * Created by Администратор on 29.04.2016.
 */
public interface CursorConverter {

    public Object convert(Cursor cursor);
}
