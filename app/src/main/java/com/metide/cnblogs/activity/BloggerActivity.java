package com.metide.cnblogs.activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.metide.cnblogs.Adapter.BlogAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.base.BaseActivity;
import com.metide.cnblogs.bean.Blog;
import com.metide.cnblogs.bean.Blogger;
import com.metide.cnblogs.biz.BlogBiz;
import com.metide.widget.CircleImageView;

import java.util.List;

public class BloggerActivity extends BaseActivity {

    private Blogger mBlogger;

    private CircleImageView mHeadImage;
    private TextView mBloggerName;

    List<Blog> mBlogs;
    private BlogAdapter mBlogAdapter;
    private RecyclerView mBlogRecyclerView;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mBlogAdapter = new BlogAdapter(getApplicationContext(), mBlogs, null);
            //设置adapter
            mBlogRecyclerView.setAdapter(mBlogAdapter);
            return true;
        }
    });


    @Override
    protected int getContentView() {
        return R.layout.activity_blogger;
    }

    @Override
    protected void initData() {
        mBlogger = (Blogger) getIntent().getSerializableExtra("blogger");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBlogs = BlogBiz.getBlogsByUser(mBlogger.blogName,1,10);
                mHandler.sendEmptyMessage(123);
            }
        }).start();
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        mHeadImage = (CircleImageView) findViewById(R.id.blogger_head_image_64);

        Glide.with(this)
                .load(mBlogger.headImage)
                .centerCrop()
                .placeholder(R.drawable.ic_blog_author)
                .crossFade()
                .into(mHeadImage);

        mBloggerName = (TextView) findViewById(R.id.blogger_head_name);
        mBloggerName.setText(mBlogger.name);

        mBlogRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_common);
        //设置布局管理器
        mBlogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //设置Item增加、移除动画
        mBlogRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mBlogRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
    }
}
