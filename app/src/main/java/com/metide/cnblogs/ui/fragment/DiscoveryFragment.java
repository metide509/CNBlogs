package com.metide.cnblogs.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metide.cnblogs.R;
import com.metide.cnblogs.interfaces.OnToWebListener;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.manager.BlogManager;
import com.metide.cnblogs.ui.activity.WebViewActivity;
import com.metide.cnblogs.ui.adapter.BlogAdapter;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class DiscoveryFragment extends Fragment implements OnToWebListener {

    private RecyclerView mRecyclerView;
    private BlogAdapter mAdapter;

    public static DiscoveryFragment newInstance() {
        DiscoveryFragment fragment = new DiscoveryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_discovery, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.discovery_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BlogAdapter(getContext());
        mAdapter.setOnToWebListener(this);
        mRecyclerView.setAdapter(mAdapter);

        final BlogManager manager = new BlogManager(getContext());

        manager.getHomeBlogs(1,10)
                .flatMap(new Function<BlogResult, ObservableSource<BlogResult.Blog>>() {
                    @Override
                    public ObservableSource<BlogResult.Blog> apply(@NonNull BlogResult blogResult) throws Exception {
                        return Observable.fromIterable(blogResult.blogs);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BlogResult.Blog>() {
                    @Override
                    public void accept(@NonNull BlogResult.Blog blog) throws Exception {
                        mAdapter.addData(blog);
                    }
                });
        return view;
    }


    @Override
    public void toWeb(View view, int position, String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
