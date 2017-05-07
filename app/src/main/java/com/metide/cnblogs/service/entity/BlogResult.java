package com.metide.cnblogs.service.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Author   metide
 * Date     2017/5/7
 */

@Root(name = "feed",strict = false)
public class BlogResult {

    @Element(name = "title")
    public String title;

    @Element(name = "id")
    public String id;

    @Element(name = "updated")
    public String updated;

    @ElementList(required = true, inline = true, entry = "entry")
    public List<Blog> blogs;

    @Root(name = "entry", strict = false)
    public static class Blog {

        /**
         *
         */
        @Element(name = "id")
        public String id;
        /**
         * 标题
         */
        @Element(name = "title")
        public String title;
        /**
         * 简述
         */
        @Element(name = "summary", required = false)
        public String summary;
        /**
         * 发布时间
         */
        @Element(name = "published")
        public String published;
        /**
         *
         */
        @Element(name = "updated")
        public String updated;

        @Element(name = "author")
        public Author author;

        /**
         * 博客链接
         */
        @Element(name = "link", required = false)
        public Link link;
        /**
         * 推荐次数
         */
        @Element(name = "diggs")
        public int diggs;
        /**
         * 浏览人数
         */
        @Element(name = "views")
        public int views;
        /**
         * 评论人数
         */
        @Element(name = "comments")
        public int comments;
    }

    @Root(name = "author")
    public static class Author{
        /**
         * 作者姓名
         */
        @Element(name = "name")
        public String name;
        /**
         * 作者链接
         */
        @Element(name = "uri")
        public String uri;
        /**
         * 作者头像
         */
        @Element(name = "avatar", required = false)
        public String avatar;
    }
}
