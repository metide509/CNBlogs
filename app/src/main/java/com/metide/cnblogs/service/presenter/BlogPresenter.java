package com.metide.cnblogs.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.manager.BlogManager;
import com.metide.cnblogs.service.view.BlogView;
import com.metide.cnblogs.service.view.View;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author   metide
 * Date     2017/5/6
 */

public class BlogPresenter implements Presenter {

    private BlogManager manager;
    private CompositeDisposable mCompositeSubscription;
    private Context mContext;
    private BlogView mBlogView;
    private BlogResult.Blog mBlog;

    Disposable dis;

    public BlogPresenter (Context mContext){
        this.mContext = mContext;
    }
    @Override
    public void onCreate() {
        manager = new BlogManager(mContext);
        mCompositeSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {
        if(!dis.isDisposed()){
            dis.dispose();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void attachView(View view) {
        mBlogView = (BlogView)view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {
    }
    public void getRecommendBloggers(int pageIndex,int pageSize){

        dis = manager.getRecommendBloggers(pageIndex,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
