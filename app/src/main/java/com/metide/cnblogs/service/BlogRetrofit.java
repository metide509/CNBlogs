package com.metide.cnblogs.service;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Author   metide
 * Date     2017/5/6
 */

public class BlogRetrofit {
    private Context mContext;

    OkHttpClient client = new OkHttpClient();
    SimpleXmlConverterFactory factory = SimpleXmlConverterFactory.create();
    private static BlogRetrofit instance = null;
    private Retrofit mRetrofit = null;
    public static BlogRetrofit getInstance(Context context){
        if (instance == null){
            instance = new BlogRetrofit(context);
        }
        return instance;
    }
    private BlogRetrofit(Context context){
        mContext = context;
        init();
    }

    private void init() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://wcf.open.cnblogs.com/blog/")
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    public BlogService getService(){
        return mRetrofit.create(BlogService.class);
    }
}
