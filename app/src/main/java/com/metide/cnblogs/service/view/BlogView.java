package com.metide.cnblogs.service.view;


import com.metide.cnblogs.service.entity.BlogResult;

/**
 * Author   metide
 * Date     2017/5/6
 */

public interface BlogView extends View {
    void onSuccess(BlogResult.Blog mBook);
    void onError(String result);
}
