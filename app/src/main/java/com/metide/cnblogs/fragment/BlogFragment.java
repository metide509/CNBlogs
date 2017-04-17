package com.metide.cnblogs.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.metide.cnblogs.Adapter.BlogAdapter;
import com.metide.cnblogs.Adapter.BloggerAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.activity.BloggerActivity;
import com.metide.cnblogs.activity.WebViewActivity;
import com.metide.cnblogs.bean.Blog;
import com.metide.cnblogs.bean.Blogger;
import com.metide.cnblogs.biz.BlogBiz;
import com.metide.cnblogs.interfaces.OnToWebListener;

import java.util.ArrayList;
import java.util.List;


public class BlogFragment extends Fragment implements Handler.Callback {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private int mNewPageNumber = 1;
    private static final int PAGE_SIZE =10;

    protected View mContentView;
    protected boolean mIsLoadedData = false;


    private LRecyclerView mRecyclerView;
    private BlogAdapter mBlogAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    private Handler mHandler = new Handler(this);

    protected static final int FIRST_LOAD = 110;
    protected static final int LOAD_COMPLETED = 111;
    protected static final int REFRESH_COMPLETED = 112;


    public static BlogFragment newInstance(String param) {
        BlogFragment fragment = new BlogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            handleOnVisibilityChangedToUser(isVisibleToUser);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(false);
        }
    }

    /**
     * 处理对用户是否可见
     *
     * @param isVisibleToUser
     */
    private void handleOnVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // 对用户可见
            if (!mIsLoadedData) {
                mIsLoadedData = true;
                onLazyLoadOnce();
            }
            onVisibleToUser();
        } else {
            // 对用户不可见
            onInvisibleToUser();
        }
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce() {
        //mNewPageNumber = 0;
        //loadData(FIRST_LOAD);

        if(mBlogAdapter == null || mBlogAdapter.getItemCount() == 0){
            List<Blog> blogs = new ArrayList<>();
            mBlogAdapter = new BlogAdapter(getContext(), blogs,
                    new OnToWebListener() {
                        @Override
                        public void toWeb(View view, int position, String url) {
                            Intent intent = new Intent(getContext(), WebViewActivity.class);
                            intent.putExtra("url",url);
                            startActivity(intent);
                        }
                    });
            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBlogAdapter);
            //设置adapter
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
            mRecyclerView.forceToRefresh();
        }
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected void onVisibleToUser() {
    }

    /**
     * 对用户不可见时触发该方法
     */
    protected void onInvisibleToUser() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            initView(inflater, container, savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
    }

    /**
     * 初始化View控件
     */
    protected void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setContentView(R.layout.fragment_blog);

        mRecyclerView = (LRecyclerView) mContentView.findViewById(R.id.lrecycler_view_common);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));

        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }
            @Override
            public void onScrollStateChanged(int state) {

            }

        });

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(REFRESH_COMPLETED);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(LOAD_COMPLETED);
            }
        });

        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.color_primary , R.color.gray_light);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.color_primary , R.color.gray_light);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");


        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);

        mRecyclerView.setArrowImageView(R.drawable.ic_arrow_down);  //设置下拉刷新箭头
    }

    /**
     * 给View控件添加事件监听器
     */
    protected void setListener(){

    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected void processLogic(Bundle savedInstanceState){

    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

    private void loadData(final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Blog> blogs = BlogBiz.getHomeBlogsByPage(mNewPageNumber, PAGE_SIZE);

                Message msg = new Message();
                msg.what = type;
                msg.obj = blogs;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what == FIRST_LOAD){
            mBlogAdapter = new BlogAdapter(getContext(), (List<Blog>) msg.obj,
                    new OnToWebListener() {
                @Override
                public void toWeb(View view, int position, String url) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                }
            });

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBlogAdapter);
            //设置adapter
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
            mNewPageNumber = mNewPageNumber + 1;
        }else if(msg.what == REFRESH_COMPLETED){
            mBlogAdapter.notifyData((List<Blog>) msg.obj);
            mNewPageNumber = mNewPageNumber + 1;
            mRecyclerView.refreshComplete(PAGE_SIZE);
            mLRecyclerViewAdapter.notifyDataSetChanged();
            //mRecyclerView.refreshComplete(PAGE_SIZE);// REQUEST_COUNT为每页加载数量
        }else if(msg.what == LOAD_COMPLETED){
            mBlogAdapter.notifyData((List<Blog>) msg.obj);
            mNewPageNumber = mNewPageNumber + 1;
            mRecyclerView.refreshComplete(PAGE_SIZE);// REQUEST_COUNT为每页加载数量
        }

        return true;
    }
}
