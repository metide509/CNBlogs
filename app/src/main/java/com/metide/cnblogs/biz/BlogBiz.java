package com.metide.cnblogs.biz;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.metide.cnblogs.utils.Logger;
import com.metide.cnblogs.bean.Blog;
import com.metide.cnblogs.bean.Blogger;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/9
 */

public class BlogBiz extends BaseBiz {

    public static final String GET_BLOG_URLS = "http://wcf.open.cnblogs.com/blog/bloggers/recommend/{PAGEINDEX}/{PAGESIZE}";
    /**
     * 获取48小时阅读排行
     *
     * 参数说明：ITEMCOUNT 要返回数目
     * 返回格式
     *
         <feed xmlns="http://www.w3.org/2005/Atom">
            <title type="text">博客园_48小时阅读排行</title>
            <id>uuid:908bf8f3-3792-4c95-abeb-f1a03df3a8a5;id=698</id>
            <updated>2017-04-14T16:14:52Z</updated>
            <entry>
                <id>6702488</id>
                <title type="text">一个只有99行代码的JS流程框架</title>
                <summary type="text">
                    文章作者通过思考一个“如何能让js代码写起来更语义化和更具有可读性？”的问题，进而一个只有99行代码的JS流程框架在作者大脑中慢慢形成，在此与大家分享此干货。
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
         </feed>
     */
    public static final String GET_48_HOURS_TOP_BLOG = "http://wcf.open.cnblogs.com/blog/48HoursTopViewPosts/{ITEMCOUNT}";

    public static final String THE_BLOGS_OF_USER = "http://wcf.open.cnblogs.com/blog/u/{BLOGAPP}/posts/{PAGEINDEX}/{PAGESIZE}";

    /**
     * 分页获取首页博客列表
     */
    public static final String GET_HOME_BLOGS_BY_PAGE = "http://wcf.open.cnblogs.com/blog/sitehome/paged/{PAGEINDEX}/{PAGESIZE}";


    /**
     *
     <entry>
     <id>6702488</id>
     <title type="text">一个只有99行代码的JS流程框架</title>
     <summary type="text">
     文章作者通过思考一个“如何能让js代码写起来更语义化和更具有可读性？”的问题，进而一个只有99行代码的JS流程框架在作者大脑中慢慢形成，在此与大家分享此干货。
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
     * @param url
     * @return
     */
    public static List<Blog> getBlogs(String url){

        InputStream is = getInputStream(url);
        if (is == null) return null;

        List<Blog> blogs = null;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            //获取解析的标签的类型
            int type = xmlPullParser.getEventType();

            Blog blog = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                //获取开始标签名字
                String tag = xmlPullParser.getName();

                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        blogs = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:

                        if ("entry".equals(tag)) {
                            blog = new Blog();
                        } else if ("id".equals(tag) && blog != null) {
                            blog.id = xmlPullParser.nextText();
                        } else if ("title".equals(tag) && blog != null) {
                            blog.title = xmlPullParser.nextText();
                        } else if ("summary".equals(tag) && blog != null) {
                            blog.summary = xmlPullParser.nextText();
                        } else if ("published".equals(tag) && blog != null) {
                            blog.time = xmlPullParser.nextText();
                        } else if ("name".equals(tag) && blog != null) {
                            blog.authorName = xmlPullParser.nextText();
                        } else if ("uri".equals(tag) && blog != null) {
                            blog.authorLink = xmlPullParser.nextText();
                        } else if ("avatar".equals(tag) && blog != null) {
                            blog.authorImage = xmlPullParser.nextText();
                            Logger.d(blog.authorImage);
                        }else if ("link".equals(tag) && blog != null) {
                            blog.link = xmlPullParser.getAttributeValue(1);
                            Logger.d(blog.link);
                        } else if ("diggs".equals(tag) && blog != null) {
                            blog.recommendation = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("views".equals(tag) && blog != null) {
                            blog.view = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("comments".equals(tag) && blog != null) {
                            blog.view = Integer.parseInt(xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("entry".equals(tag)){
                            blogs.add(blog);
                            blog = null;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        is.close();
                        break;
                }
                //细节：
                type = xmlPullParser.next();
            }


        } catch (Exception e) {
            Log.d("出旬了", "" + e.getMessage());
        }finally {
            return blogs;
        }
    }


    public static List<Blog> getBlogs(int count) {
            String url = GET_48_HOURS_TOP_BLOG.replace("{ITEMCOUNT}", Integer.toString(count));
            return getBlogs(url);
    }

    public static List<Blog> getHomeBlogsByPage(int pageIndex, int pageSize) {
        String url = GET_HOME_BLOGS_BY_PAGE
                .replace("{PAGEINDEX}", Integer.toString(pageIndex))
                .replace("{PAGESIZE}",Integer.toString(pageSize));
        return getBlogs(url);
    }


    /**
     *
     * 分页获取推荐博客列表
     *
     *
     *
     <feed xmlns="http://www.w3.org/2005/Atom">
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
     </feed>
     *
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static List<Blogger> getBloggers(int pageIndex, int pageSize){

        String url = GET_BLOG_URLS
                .replace("{PAGEINDEX}",Integer.toString(pageIndex))
                .replace("{PAGESIZE}", Integer.toString(pageSize));

        InputStream is = getInputStream(url);

        if(is == null) return null;

        List<Blogger> bloggers = null;

        XmlPullParser xmlPullParser= Xml.newPullParser();
        try {
            xmlPullParser.setInput(is,"UTF-8");
            //获取解析的标签的类型
            int type=xmlPullParser.getEventType();


            Blogger blogger = null;
            while(type != XmlPullParser.END_DOCUMENT){

                String tag =xmlPullParser.getName();
                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        bloggers = new ArrayList<>();
                    case XmlPullParser.START_TAG:
                        //获取开始标签名字
                        Logger.d(tag);

                        if("entry".equals(tag)){
                            blogger = new Blogger();
                        }else if("id".equals(tag) && blogger != null){
                            blogger.id = xmlPullParser.nextText();
                        }else if("title".equals(tag) && blogger != null){
                            blogger.name = xmlPullParser.nextText();
                        }else if("updated".equals(tag) && blogger != null){
                            blogger.lastTime = xmlPullParser.nextText();
                        }else if("link".equals(tag) && blogger != null){
                            blogger.link = xmlPullParser.nextText();
                        }else if("blogapp".equals(tag) && blogger != null){
                            blogger.blogName = xmlPullParser.nextText();
                        }else if("avatar".equals(tag) && blogger != null){
                            blogger.headImage = xmlPullParser.nextText();
                        }else if("postcount".equals(tag) && blogger != null){
                            blogger.postCount = Integer.parseInt(xmlPullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("entry".equals(tag)){
                            bloggers.add(blogger);
                            blogger = null;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        is.close();
                }
                //细节：
                type = xmlPullParser.next();
            }
        } catch (Exception e) {
            Logger.e(e);
        }finally {
            return bloggers;
        }
    }


    public static List<Blog> getBlogsByUser(String blogName, int pageIndex, int pageSize){
        if(TextUtils.isEmpty(blogName)) return null;
        String url = THE_BLOGS_OF_USER
                .replace("{BLOGAPP}", blogName)
                .replace("{PAGEINDEX}", Integer.toString(pageIndex))
                .replace("{PAGESIZE}", Integer.toString(pageSize));

        return getBlogs(url);
    }
}
