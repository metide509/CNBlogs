package com.metide.cnblogs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.metide.cnblogs.Adapter.BloggerAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.activity.BloggerActivity;
import com.metide.cnblogs.base.BaseFragment;
import com.metide.cnblogs.bean.Blogger;
import com.metide.cnblogs.biz.BlogBiz;

import java.util.List;


public class BloggerFragment extends BaseFragment {

    private BloggerAdapter mBlogAdapter;
    private int curPage = 1;
    private static final int PAGE_SIZE =20;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 123){
                mBlogAdapter = new BloggerAdapter(getContext(), (List<Blogger>) msg.obj, new BloggerAdapter.OnBlogItemClickListener() {
                    @Override
                    public void onClick(View view, int position, Blogger blogger) {
                        Intent intent = new Intent(getContext(), BloggerActivity.class);
                        intent.putExtra("blogger",blogger);
                        startActivity(intent);
                    }
                });
                //设置adapter
                mRecyclerView.setAdapter(mBlogAdapter);
                curPage = curPage + 1;
            }else if(msg.what == 234){
                mBlogAdapter.notifyData((List<Blogger>) msg.obj);
                curPage = curPage + 1;
            }

            return true;
        }
    });

    public static BloggerFragment newInstance() {
        BloggerFragment fragment = new BloggerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Blogger> bloggers = BlogBiz.getBloggers(curPage, PAGE_SIZE);
                Message msg = new Message();
                msg.what = 123;
                msg.obj = bloggers;
                mHandler.sendMessage(msg);
            }
        }).start();

    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_blogger;
    }

    @Override
    protected void setAdapter() {

    }
}
