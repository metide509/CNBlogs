package com.metide.cnblogs.bean;

/**
 * Author   metide
 * Date     2017/4/9
 */

public class Blog {


    public String id;
    /**
     * 标题
     */
    public String title;
    /**
     * 博客链接
     */
    public String link;
    /**
     * 简述
     */
    public String summary;
    /**
     * 作者头像
     */
    public String authorImage;
    /**
     * 作者姓名
     */
    public String authorName;
    /**
     * 作者链接
     */
    public String authorLink;
    /**
     * 发布时间
     */
    public String time;
    /**
     * 评论人数
     */
    public int comment;
    /**
     * 浏览人数
     */
    public int view;
    /**
     * 推荐次数
     */
    public int recommendation;


    public static Blog clone(Blog blog){
        if(blog == null) return null;

        Blog temp = new Blog();

        temp.id = blog.id;
        temp.view = blog.view;
        temp.title = blog.title;
        temp.link = blog.link;
        temp.summary = blog.summary;
        temp.authorImage = blog.authorImage;
        temp.authorLink = blog.authorLink;
        temp.authorName = blog.authorName;
        temp.time = blog.time;
        temp.comment = blog.comment;
        temp.recommendation = blog.recommendation;

        return temp;
    }

    public Blog clone(){
        return clone(this);
    }
}
