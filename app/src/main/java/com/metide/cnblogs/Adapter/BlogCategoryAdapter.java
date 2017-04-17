package com.metide.cnblogs.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.metide.cnblogs.R;
import com.metide.cnblogs.fragment.BlogFragment;

/**
 * Author   metide
 * Date     2017/4/16
 */

public class BlogCategoryAdapter extends FragmentPagerAdapter {

    private String[] mCategorys;

    public final int COUNT = 5;
    private String[] titles = new String[]{"Tab1", "Tab2", "Tab3", "Tab4", "Tab5"};
    private Context context;

    public BlogCategoryAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mCategorys = context.getResources().getStringArray(R.array.home_category_name);
    }

    @Override
    public Fragment getItem(int position) {
        return BlogFragment.newInstance(mCategorys[position]);
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
