package com.metide.cnblogs.service;

import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.entity.BloggerResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Author   metide
 * Date     2017/5/6
 */

public interface BlogService {


    /**
     * 获取48小时阅读排行博客列表
     * http://wcf.open.cnblogs.com/blog/48HoursTopViewPosts/{ITEMCOUNT}
     *
     * @param itemCount 返回数目
     * @return 博客列表
     *
     <BlogResult xmlns="http://www.w3.org/2005/Atom">
         <title type="text">博客园_48小时阅读排行</title>
         <id>uuid:908bf8f3-3792-4c95-abeb-f1a03df3a8a5;id=698</id>
         <updated>2017-04-14T16:14:52Z</updated>
         <entry>
             <id>6702488</id>
             <title type="text">一个只有99行代码的JS流程框架</title>
             <summary type="text">
                文章作者通过思考一个“如何能让js代码写起来更语义化和更具有可读性？”的问题，
                进而一个只有99行代码的JS流程框架在作者大脑中慢慢形成，在此与大家分享此干货。
             </summary>
             <published>2017-04-13T10:04:00+08:00</published>
             <updated>2017-04-14T16:14:52Z</updated>
             <author>
                 <name>腾讯云技术社区</name>
                 <uri>http://www.cnblogs.com/qcloud1001/</uri>
                 <avatar>
                    http://pic.cnblogs.com/face/1112679/20170307164131.png
                 </avatar>
             </author>
             <link rel="alternate" href="http://www.cnblogs.com/qcloud1001/p/6702488.html"/>
             <diggs>34</diggs>
             <views>3562</views>
             <comments>15</comments>
         </entry>
     </BlogResult>
     */
    @GET("48HoursTopViewPosts/{ITEMCOUNT}")
    Observable<BlogResult> get48HoursTopViewBlogs(@Path("ITEMCOUNT") int itemCount);

    /**
     * 分页获取推荐博主列表
     * http://wcf.open.cnblogs.com/blog/bloggers/recommend/{PAGEINDEX}/{PAGESIZE}
     *
     * @param pageIndex 页码
     * @param pageSize 每页数目
     * @return 博主列表
     *
     <BlogResult xmlns="http://www.w3.org/2005/Atom">
        <title type="text">博客园_推荐博客</title>
        <id>uuid:edc79aaa-e29e-418b-860c-1ae01409544d;id=9547</id>
        <updated>2017-04-11T16:28:54Z</updated>
        <entry>
            <id>http://www.cnblogs.com/fish-li/</id>
            <title type="text">Fish Li</title>
            <updated>2013-11-18T14:31:32+08:00</updated>
            <link rel="alternate" href="http://www.cnblogs.com/fish-li/"/>
            <blogapp>fish-li</blogapp>
            <avatar>
            http://pic.cnblogs.com/face/u281816.png?id=28134852
            </avatar>
            <postcount>60</postcount>
        </entry>
    </BlogResult>
     */
    @GET("bloggers/recommend/{PAGEINDEX}/{PAGESIZE}")
    Observable<BloggerResult> getRecommendBloggers(@Path("PAGEINDEX") int pageIndex,
                                                @Path("PAGESIZE") int pageSize);

    /**
     * 获取推荐博客总数
     * http://wcf.open.cnblogs.com/blog/bloggers/recommend/count
     *
     * @return 推荐博客总数
     */
    @GET("bloggers/recommend/count")
    Observable<Integer> getRecommendCount();


    /**
     * 根据作者名搜索博主
     *
     * http://wcf.open.cnblogs.com/blog/bloggers/search?t={TERM}
     * @return 博主
     */
    @GET("bloggers/search")
    Observable<BloggerResult.Blogger> getBloggerByName(@Query("t") String bloggerName);


    /**
     * 获取文章评论
     *
     * http://wcf.open.cnblogs.com/blog/post/{POSTID}/comments/{PAGEINDEX}/{PAGESIZE}
     * @param postId
     * @param pageIndex
     * @param pageSize
     * @return 博主
     */
    @GET("post/{POSTID}/comments/{PAGEINDEX}/{PAGESIZE}")
    Observable<BloggerResult.Blogger> getCommentsByPostId(@Path("POSTID") String postId,
                                            @Path("PAGEINDEX") int pageIndex,
                                            @Path("PAGESIZE") int pageSize);

    /**
     * 获取文章内容
     *
     * http://wcf.open.cnblogs.com/blog/post/body/{POSTID}
     * @param postId
     * @return 文章内容
     */
    @GET("post/body/{POSTID}")
    Observable<String> getBlogByPostId(@Path("POSTID") String postId);

    /**
     * 分页获取首页文章列表
     *
     * http://wcf.open.cnblogs.com/blog/sitehome/paged/{PAGEINDEX}/{PAGESIZE}
     * @param pageIndex
     * @param pageSize
     * @return 首页文章列表
     */
    @GET("sitehome/paged/{PAGEINDEX}/{PAGESIZE}")
    Observable<BlogResult> getHomeBlogs( @Path("PAGEINDEX") int pageIndex,
                                             @Path("PAGESIZE") int pageSize);

    /**
     * 获取首页文章列表
     *
     *  http://wcf.open.cnblogs.com/blog/sitehome/recent/{ITEMCOUNT}
     * @param itemCount
     * @return 首页文章列表
     */
    @GET("sitehome/recent/{ITEMCOUNT}")
    Observable<BlogResult> getHomeBlogs( @Path("ITEMCOUNT") int itemCount);

    /**
     * 10天内推荐排行
     *
     *  http://wcf.open.cnblogs.com/blog/TenDaysTopDiggPosts/{ITEMCOUNT}
     * @param itemCount
     * @return 文章列表
     */
    @GET("TenDaysTopDiggPosts/{ITEMCOUNT}")
    Observable<BlogResult> getTenDaysTopBlogs( @Path("ITEMCOUNT") int itemCount);

    /**
     * 分页获取个人博客文章列表
     *
     *   http://wcf.open.cnblogs.com/blog/u/{BLOGAPP}/posts/{PAGEINDEX}/{PAGESIZE}
     * @param blogApp
     * @param pageIndex
     * @param pageSize
     * @return 文章列表
     */
    @GET("u/{BLOGAPP}/posts/{PAGEINDEX}/{PAGESIZE}")
    Observable<BlogResult> getTenDaysTopBlogs( @Path("BLOGAPP") String blogApp,
                                            @Path("PAGEINDEX") int pageIndex,
                                            @Path("PAGESIZE") int pageSize);
}
