package com.metide.cnblogs.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metide.cnblogs.ui.adapter.BlogCategoryAdapter;
import com.metide.cnblogs.R;

public class HomeFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;



    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        createViewPager(view);
        return view;
    }

    private void createViewPager(View view){
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_home_view_pager);
        mViewPager.setAdapter(new BlogCategoryAdapter(getActivity().getSupportFragmentManager(),getContext()));
        mTabLayout = (TabLayout) view.findViewById(R.id.fragment_home_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
