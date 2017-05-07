package com.metide.cnblogs.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metide.cnblogs.service.entity.BloggerResult;
import com.metide.cnblogs.service.manager.BlogManager;
import com.metide.cnblogs.ui.adapter.BloggerAdapter;
import com.metide.cnblogs.R;
import com.metide.cnblogs.ui.activity.BloggerActivity;
import com.metide.cnblogs.base.BaseFragment;
import com.metide.cnblogs.utils.AppSchedulers;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class BloggerFragment extends BaseFragment {

    private BloggerAdapter mBlogAdapter;
    private int curPage = 1;
    private static final int PAGE_SIZE =20;

    protected RecyclerView mRecyclerView;
    private BlogManager mBlogManager;

    public static BloggerFragment newInstance() {
        BloggerFragment fragment = new BloggerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlogManager = new BlogManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_common);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.HORIZONTAL));

        mBlogAdapter = new BloggerAdapter(getContext(), new BloggerAdapter.OnBlogItemClickListener() {
            @Override
            public void onClick(View view, int position, BloggerResult.Blogger blogger) {
                Intent intent = new Intent(getContext(), BloggerActivity.class);
                intent.putExtra("blogger",blogger);
                startActivity(intent);
            }
        });
        //设置adapter
        mRecyclerView.setAdapter(mBlogAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlogManager.getRecommendBloggers(1,20)
                .map(new Function<BloggerResult, List<BloggerResult.Blogger>>() {
                    @Override
                    public List<BloggerResult.Blogger> apply(@NonNull BloggerResult bloggerResult) throws Exception {
                        return bloggerResult.bloggers;
                    }
                })
                .observeOn(AppSchedulers.main())
                .subscribe(new Consumer<List<BloggerResult.Blogger>>() {
                    @Override
                    public void accept(@NonNull List<BloggerResult.Blogger> bloggers) throws Exception {
                        mBlogAdapter.addDatas(bloggers);
                    }
                });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_blogger;
    }

    @Override
    protected void setAdapter() {

    }
}
