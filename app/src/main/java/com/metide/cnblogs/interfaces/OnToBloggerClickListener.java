package com.metide.cnblogs.interfaces;

import android.view.View;
import com.metide.cnblogs.service.entity.BloggerResult;

/**
 * Author   metide
 * Date     2017/4/17
 */

public interface OnToBloggerClickListener {
    void toBlogger(View view, int position, BloggerResult.Blogger blogger);
}
