package com.metide.cnblogs.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.entity.BloggerResult;
import com.metide.cnblogs.ui.adapter.BlogAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.base.BaseActivity;
import com.metide.widget.CircleImageView;

import java.util.List;

public class BloggerActivity extends BaseActivity {

    private BloggerResult.Blogger mBlogger;

    private CircleImageView mHeadImage;
    private TextView mBloggerName;

    List<BlogResult.Blog> mBlogs;
    private BlogAdapter mBlogAdapter;
    private RecyclerView mBlogRecyclerView;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mBlogAdapter.setDatas(mBlogs);
            return true;
        }
    });


    @Override
    protected int getContentView() {
        return R.layout.activity_blogger;
    }

    @Override
    protected void initData() {
        mBlogger = (BloggerResult.Blogger) getIntent().getSerializableExtra("blogger");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(123);
            }
        }).start();
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        mHeadImage = (CircleImageView) findViewById(R.id.blogger_head_image_64);

        Glide.with(this)
                .load(mBlogger.avatar)
                .centerCrop()
                .placeholder(R.drawable.ic_blog_author)
                .crossFade()
                .into(mHeadImage);

        mBloggerName = (TextView) findViewById(R.id.blogger_head_name);
        mBloggerName.setText(mBlogger.title);

        mBlogRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_common);
        //设置布局管理器
        mBlogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //设置Item增加、移除动画
        mBlogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mBlogRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
        mBlogAdapter = new BlogAdapter(getApplicationContext());
        //设置adapter
        mBlogRecyclerView.setAdapter(mBlogAdapter);
    }
}
