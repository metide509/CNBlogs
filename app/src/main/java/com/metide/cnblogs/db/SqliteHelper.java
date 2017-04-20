package com.metide.cnblogs.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.metide.cnblogs.utils.Logger;

/**
 * Author   metide
 * Date     2017/4/15
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "cnblogs.db";


    private static SQLiteDatabase sqLiteDatabase;

    public synchronized static void init(Context context){
        if(sqLiteDatabase == null){
            SQLiteOpenHelper sqLiteOpenHelper = new SqliteHelper(context);
            try{
                sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            }catch (Exception e){
                sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
                Logger.e(e);
            }
        }
    }

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
        createBlogTable(db);
        createBloggerTable(db);
    }

    private void createBloggerTable(SQLiteDatabase db){
        String sql = "create table blogger(" +
                "id text primary key," +
                "name text," +
                "lastTime text," +
                "link text," +
                "blogName text," +
                "headImage text," +
                "postCount integer," +
                "localHeadImage text)";

        db.execSQL(sql);
    }

    private void createBlogTable(SQLiteDatabase db){
        String sql = "create table blog(" +
                "id text primary key," +
                "title text," +
                "link text," +
                "summary text," +
                "authorImage text," +
                "authorName text," +
                "authorLink text," +
                "time text," +
                "comment integer," +
                "view integer," +
                "recommendation integer)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static void exeSQL(String sql, Object... args){
        vetyfy(sql,"sql could not be null or empty");
        vetyfy(sqLiteDatabase,"please init SQLiteDatabase first");
        sqLiteDatabase.execSQL(sql, args);
    }

    public static Cursor rawQuery(String sql, String...args){
        vetyfy(sql,"sql could not be null or empty");
        vetyfy(sqLiteDatabase,"please init SQLiteDatabase first");
        return sqLiteDatabase.rawQuery(sql, args);
    }

    private static  <T extends Object> void vetyfy(T t, String message){
        if(t == null){
            throw new NullPointerException(message);
        }
    }
}
