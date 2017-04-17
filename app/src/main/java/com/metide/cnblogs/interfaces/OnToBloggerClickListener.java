package com.metide.cnblogs.interfaces;

import android.view.View;

import com.metide.cnblogs.bean.Blogger;

/**
 * Author   metide
 * Date     2017/4/17
 */

public interface OnToBloggerClickListener {
    void toBlogger(View view, int position, Blogger blogger);
}
