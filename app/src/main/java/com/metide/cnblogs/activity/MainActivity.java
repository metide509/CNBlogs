package com.metide.cnblogs.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.metide.cnblogs.R;
import com.metide.cnblogs.fragment.BloggerFragment;
import com.metide.cnblogs.fragment.DiscoveryFragment;
import com.metide.cnblogs.fragment.HomeFragment;
import com.metide.cnblogs.fragment.MyFragment;
import com.metide.cnblogs.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar mNavigationBar;
    private List<Fragment> mFragments;
    private String[] mNavNames;
    private int[] mActiveIcons = {
            R.drawable.ic_nav_home_avitve,
            R.drawable.ic_nav_groups_active,
            R.drawable.ic_nav_new_active,
            R.drawable.ic_nav_discovery_active,
            R.drawable.ic_nav_my_active
    };
    private int[] mInactiveIcons = {
            R.drawable.ic_nav_home_inactive,
            R.drawable.ic_nav_groups_inactive,
            R.drawable.ic_nav_new_inactive,
            R.drawable.ic_nav_discovery_inactive,
            R.drawable.ic_nav_my_inactive
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView(savedInstanceState);
    }

    protected void initView(Bundle savedInstanceState){
        initBottomNavigationBar();
        initFragments();
        setDefaultFragment(mFragments.get(0));
    }

    protected void initData(){
        mNavNames = getResources().getStringArray(R.array.nav_button_name);
    }

    private void initBottomNavigationBar(){
        mNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        mNavigationBar.clearAll();
//        mNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
//        mNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

//        mNavigationBar
//                .addItem(new BottomNavigationItem(R.drawable.ic_home_32, "首页"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_bloger_32, "博主"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_news_32, "新闻"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_discovery_32, "发现"))
//                .addItem(new BottomNavigationItem(R.drawable.ic_me_32, "我"))
//                .setFirstSelectedPosition(2)
//                .initialise();

        for(int i = 0; i < mNavNames.length; i++){
            BottomNavigationItem item = new BottomNavigationItem(mActiveIcons[i], mNavNames[i]);
            item.setInactiveIcon(ContextCompat.getDrawable(this, mInactiveIcons[i]));
            mNavigationBar.addItem(item);
        }
        mNavigationBar
                .setTabSelectedListener(this)
                .initialise();
    }


    /**
     * 设置默认的
     */
    private void setDefaultFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.commit();
    }

    private void initFragments() {
        if(mFragments == null){
            mFragments = new ArrayList<>();
            mFragments.add(HomeFragment.newInstance());
            mFragments.add(BloggerFragment.newInstance());
            mFragments.add(NewsFragment.newInstance());
            mFragments.add(DiscoveryFragment.newInstance());
            mFragments.add(MyFragment.newInstance());
        }
    }

    @Override
    public void onTabSelected(int position) {
        if (mFragments != null) {
            if (position < mFragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = mFragments.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.main_frame_layout, fragment);
                } else {
                    ft.add(R.id.main_frame_layout, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }

    }

    @Override
    public void onTabUnselected(int position) {
        if (mFragments != null) {
            if (position < mFragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = mFragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }
}
