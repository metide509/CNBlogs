package com.metide.cnblogs.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.metide.cnblogs.db.BlogDB;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.manager.BlogManager;
import com.metide.cnblogs.ui.adapter.BlogAdapter;
import com.metide.cnblogs.ui.adapter.CommonAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.ui.activity.WebViewActivity;
import com.metide.cnblogs.interfaces.OnToWebListener;
import com.metide.cnblogs.utils.AppSchedulers;
import com.metide.cnblogs.utils.Logger;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class BlogFragment extends Fragment
        implements Handler.Callback,CommonAdapter.OnItemClickListener<BlogResult.Blog>,OnToWebListener {

    private static final String ARG_TYPE = "BLOG_TYPE";

    private int mType;

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


    private BlogManager mBlogManger;

    public static BlogFragment newInstance(int type) {
        BlogFragment fragment = new BlogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }

        mBlogManger = new BlogManager(getContext());
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
        mRecyclerView.forceToRefresh();
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

        mRecyclerView = getViewById (R.id.recycler_view_common);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL));

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

        mBlogAdapter = new BlogAdapter(getContext());
        mBlogAdapter.setOnToWebListener(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBlogAdapter);
        //设置adapter
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
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

    private void loadData(int type){
        mBlogManger.getHomeBlogs(mNewPageNumber, PAGE_SIZE)
                .map(new Function<BlogResult, List<BlogResult.Blog>>() {
                    @Override
                    public List<BlogResult.Blog> apply(@NonNull BlogResult blogResult) throws Exception {
                        Logger.d(Thread.currentThread().getName());
                        return blogResult.blogs;
                    }
                })
                .subscribeOn(AppSchedulers.io())
                .map(new Function<List<BlogResult.Blog>, List<BlogResult.Blog>>() {
                    @Override
                    public List<BlogResult.Blog> apply(@NonNull List<BlogResult.Blog> blogs) throws Exception {
                        mBlogAdapter.addDatas(blogs);
                        mRecyclerView.refreshComplete(PAGE_SIZE);
                        Logger.d(Thread.currentThread().getName());
                        return blogs;
                    }
                })
                .subscribeOn(AppSchedulers.main())
                .flatMap(new Function<List<BlogResult.Blog>, ObservableSource<BlogResult.Blog>>() {
                    @Override
                    public ObservableSource<BlogResult.Blog> apply(@NonNull List<BlogResult.Blog> blogs) throws Exception {
                        Logger.d(Thread.currentThread().getName());
                        return Observable.fromIterable(blogs);
                    }
                })
                .observeOn(AppSchedulers.io())
                .subscribe(new Consumer<BlogResult.Blog>() {
                    @Override
                    public void accept(@NonNull BlogResult.Blog blog) throws Exception {
                        Logger.d(Thread.currentThread().getName());
                        BlogDB.save(blog);
                    }
                });
    }

    @Override
    public boolean handleMessage(Message msg) {

        if(msg.what == FIRST_LOAD){
            mBlogAdapter.setDatas((List<BlogResult.Blog>)msg.obj);
            mNewPageNumber = mNewPageNumber + 1;
            mRecyclerView.refreshComplete(PAGE_SIZE);
            mLRecyclerViewAdapter.notifyDataSetChanged();
        }else if(msg.what == REFRESH_COMPLETED){

            mBlogAdapter.addDatas((List<BlogResult.Blog>) msg.obj);
            mNewPageNumber = mNewPageNumber + 1;
            mRecyclerView.refreshComplete(PAGE_SIZE);
            mLRecyclerViewAdapter.notifyDataSetChanged();

        }else if(msg.what == LOAD_COMPLETED){

            mBlogAdapter.addDatas((List<BlogResult.Blog>) msg.obj);
            mNewPageNumber = mNewPageNumber + 1;
            mRecyclerView.refreshComplete(PAGE_SIZE);

        }

        return true;
    }

    @Override
    public void onClick(View view, int position, BlogResult.Blog blog) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url",blog.link.href);
        startActivity(intent);
    }

    @Override
    public void toWeb(View view, int position, String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
