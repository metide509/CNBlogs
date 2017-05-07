package com.metide.cnblogs.service.manager;

import android.content.Context;

import com.metide.cnblogs.service.BlogRetrofit;
import com.metide.cnblogs.service.BlogService;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.entity.BloggerResult;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author   metide
 * Date     2017/5/6
 */

public class BlogManager {

    private BlogService mService;
    public BlogManager(Context context){
        this.mService = BlogRetrofit.getInstance(context).getService();
    }

    /**
     * 获取48小时阅读排行博客列表
     * http://wcf.open.cnblogs.com/blog/48HoursTopViewPosts/{ITEMCOUNT}
     *
     * @param itemCount 返回数目
     * @return 博客列表
     */
    public Observable<BlogResult> get48HoursTopViewBlogs(int itemCount){
        return mService.get48HoursTopViewBlogs(itemCount)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 分页获取推荐博主列表
     * http://wcf.open.cnblogs.com/blog/bloggers/recommend/{PAGEINDEX}/{PAGESIZE}
     *
     * @param pageIndex 页码
     * @param pageSize 每页数目
     * @return 博主列表
     */
    public Observable<BloggerResult> getRecommendBloggers(int pageIndex, int pageSize){
        return mService.getRecommendBloggers(pageIndex, pageSize)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取推荐博客总数
     * http://wcf.open.cnblogs.com/blog/bloggers/recommend/count
     *
     * @return 推荐博客总数
     */
    public Observable<Integer> getRecommendCount(){
        return mService.getRecommendCount();
    }


    /**
     * 根据作者名搜索博主
     *
     * http://wcf.open.cnblogs.com/blog/bloggers/search?t={TERM}
     * @return 博主
     */
    public Observable<BloggerResult.Blogger> getBloggerByName(String bloggerName){
        return mService.getBloggerByName(bloggerName);
    }


    /**
     * 获取文章评论
     *
     * http://wcf.open.cnblogs.com/blog/post/{POSTID}/comments/{PAGEINDEX}/{PAGESIZE}
     * @param postId
     * @param pageIndex
     * @param pageSize
     * @return 博主
     */
    public Observable<BloggerResult.Blogger> getCommentsByPostId(String postId,int pageIndex,int pageSize){
        return mService.getCommentsByPostId(postId, pageIndex, pageSize);
    }

    /**
     * 获取文章内容
     *
     * http://wcf.open.cnblogs.com/blog/post/body/{POSTID}
     * @param postId
     * @return 文章内容
     */
    public Observable<String> getBlogByPostId(String postId){
        return mService.getBlogByPostId(postId);
    }

    /**
     * 分页获取首页文章列表
     *
     * http://wcf.open.cnblogs.com/blog/sitehome/paged/{PAGEINDEX}/{PAGESIZE}
     * @param pageIndex
     * @param pageSize
     * @return 首页文章列表
     */
    public Observable<BlogResult> getHomeBlogs(int pageIndex,int pageSize){
        return mService.getHomeBlogs(pageIndex, pageSize).subscribeOn(Schedulers.io());
    }

    /**
     * 获取首页文章列表
     *
     *  http://wcf.open.cnblogs.com/blog/sitehome/recent/{ITEMCOUNT}
     * @param itemCount
     * @return 首页文章列表
     */
    public Observable<BlogResult> getHomeBlogs(int itemCount){
        return mService.getHomeBlogs(itemCount).subscribeOn(Schedulers.io());
    }

    /**
     * 10天内推荐排行
     *
     *  http://wcf.open.cnblogs.com/blog/TenDaysTopDiggPosts/{ITEMCOUNT}
     * @param itemCount
     * @return 文章列表
     */
    public Observable<BlogResult> getTenDaysTopBlogs(int itemCount){
        return mService.getTenDaysTopBlogs(itemCount);
    }

    /**
     * 分页获取个人博客文章列表
     *
     *   http://wcf.open.cnblogs.com/blog/u/{BLOGAPP}/posts/{PAGEINDEX}/{PAGESIZE}
     * @param blogApp
     * @param pageIndex
     * @param pageSize
     * @return 文章列表
     */
    public Observable<BlogResult> getTenDaysTopBlogs(String blogApp,int pageIndex,int pageSize){
        return mService.getTenDaysTopBlogs(blogApp, pageIndex, pageSize);
    }
}
