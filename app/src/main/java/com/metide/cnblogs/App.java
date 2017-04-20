package com.metide.cnblogs;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.metide.cnblogs.db.SqliteHelper;

/**
 * Author   metide
 * Date     2017/4/19
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SqliteHelper.init(getApplicationContext());
        Stetho.initializeWithDefaults(getApplicationContext());
    }
}
