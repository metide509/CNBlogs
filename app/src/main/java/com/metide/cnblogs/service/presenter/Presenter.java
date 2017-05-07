package com.metide.cnblogs.service.presenter;

import android.content.Intent;

import com.metide.cnblogs.service.view.View;

/**
 * Author   metide
 * Date     2017/5/6
 */

public interface Presenter {

    void onCreate();

    void onStart();

    void onResume();

    void onStop();

    void pause();

    void attachView(View view);

    void attachIncomingIntent(Intent intent);
}
