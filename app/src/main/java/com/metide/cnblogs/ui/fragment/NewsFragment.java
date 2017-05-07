package com.metide.cnblogs.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

import com.metide.cnblogs.ui.adapter.NewsAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.ui.activity.WebViewActivity;
import com.metide.cnblogs.base.BaseFragment;
import com.metide.cnblogs.service.entity.News;
import com.metide.cnblogs.biz.NewsBiz;
import com.metide.cnblogs.interfaces.OnToWebListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends BaseFragment {
    List<News> mNews;
    private NewsAdapter mNewsAdapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mNewsAdapter = new NewsAdapter(getContext(), mNews, new OnToWebListener() {
                @Override
                public void toWeb(View view, int position, String url) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            });
            //设置adapter
            mRecyclerView.setAdapter(mNewsAdapter);
            return true;
        }
    });

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mNews = NewsBiz.getNews(20);
                mHandler.sendEmptyMessage(123);
            }
        }).start();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void setAdapter() {

    }

}
