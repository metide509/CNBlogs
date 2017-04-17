package com.metide.cnblogs.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author   metide
 * Date     2017/4/15
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "cnblogs.db";

    public SqliteHelper(Context context) {
        this(context, new DatabaseErrorHandler() {
            @Override
            public void onCorruption(SQLiteDatabase dbObj) {

            }
        });
    }

    public SqliteHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, null, VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
