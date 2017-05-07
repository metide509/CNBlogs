package com.metide.cnblogs.service.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Author   metide
 * Date     2017/5/7
 */
@Root(name = "feed", strict = false)
public class BloggerResult {
    @Element(name = "title")
    public String title;
    @Element(name = "id")
    public String id;
    @Element(name = "updated")
    public String updated;

    @ElementList(required = true, inline = true, entry = "entry")
    public List<Blogger> bloggers;

    @Root(name = "entry")
    public static class Blogger implements Serializable{
        @Element(name = "id")
        public String id;
        @Element(name = "title")
        public String title;
        @Element(name = "updated")
        public String updated;
        @Element(name = "link")
        public Link link;
        @Element(name = "blogapp")
        public String blogApp;
        @Element(name = "avatar")
        public String avatar;
        @Element(name = "postcount")
        public int postCount;
    }
}
