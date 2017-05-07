package com.metide.cnblogs.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.metide.cnblogs.R;
import com.metide.cnblogs.ui.fragment.BlogFragment;

/**
 * Author   metide
 * Date     2017/4/16
 */

public class BlogCategoryAdapter extends FragmentPagerAdapter {

    private String[] mCategorys;

    private Context context;

    public BlogCategoryAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mCategorys = context.getResources().getStringArray(R.array.home_category_name);
    }

    @Override
    public Fragment getItem(int position) {
        return BlogFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mCategorys.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategorys[position];
    }
}
