package com.metide.cnblogs.biz;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.metide.cnblogs.utils.Logger;
import com.metide.cnblogs.bean.News;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/15
 */

public class NewsBiz extends BaseBiz{



    public static final String GET_RECENT_NEWS = "http://wcf.open.cnblogs.com/news/recent/{ITEMCOUNT}";


    public static List<News> getNews(int count) {

        String url = GET_RECENT_NEWS.replace("{ITEMCOUNT}", Integer.toString(count));
        return getNews(url);
    }

    /**
     *
     *
     <feed xmlns="http://www.w3.org/2005/Atom">
     <title type="text">博客园新闻频道</title>
     <id>uuid:908bf8f3-3792-4c95-abeb-f1a03df3a8a5;id=1787</id>
     <updated>2017-04-14T18:30:59Z</updated>
     <link href="http://news.cnblogs.com/"/>
     <entry>
         <id>567180</id>
         <title type="text">广告屏蔽插件Adblock减少广告收入 还会降低流量</title>
         <summary type="text">
            据 PageFair 最新报告显示，著名广告屏蔽插件 Adblock 对网站的流量和收入都有巨大影响。PageFair 是一家专门提供反 Adblock 软件的公司，所以也是利益相关方，发布这样的报告不足为奇。据他们称，本报告是对本杰明&middot;希勒教授（布兰迪斯大学）、乔尔&...
         </summary>
         <published>2017-04-14T23:36:38+08:00</published>
         <updated>2017-04-14T18:30:59Z</updated>
         <link rel="alternate" href="http://news.cnblogs.com/n/567180/"/>
         <diggs>0</diggs>
         <views>54</views>
         <comments>0</comments>
         <topic/>
             <topicIcon>
                http://images0.cnblogs.com/news_topic///images2015.cnblogs.com/news_topic/20160527093258428-1943766924.png
             </topicIcon>
         <sourceName>网易科技</sourceName>
     </entry>
     </feed>
     *
     *
     * @param url
     * @return
     */
    public static List<News> getNews(String url){

        InputStream is = getInputStream(url);
        if (is == null) return null;

        List<News> newses = null;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            //获取解析的标签的类型
            int type = xmlPullParser.getEventType();

            News news = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                //获取开始标签名字
                String tag = xmlPullParser.getName();

                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        newses = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:

                        if ("entry".equals(tag)) {
                            news = new News();
                        } else if ("id".equals(tag) && news != null) {
                            news.id = xmlPullParser.nextText();
                        } else if ("title".equals(tag) && news != null) {
                            news.title = xmlPullParser.nextText();
                        } else if ("summary".equals(tag) && news != null) {
                            news.summary = xmlPullParser.nextText();
                        } else if ("published".equals(tag) && news != null) {
                            news.time = xmlPullParser.nextText();
                        } else if ("link".equals(tag) && news != null) {
                            news.link = xmlPullParser.getAttributeValue(1);
                            Logger.d(news.link);
                        } else if ("diggs".equals(tag) && news != null) {
                            news.recommendation = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("views".equals(tag) && news != null) {
                            news.view = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("comments".equals(tag) && news != null) {
                            news.view = Integer.parseInt(xmlPullParser.nextText());
                        }else if("topicIcon".equals(tag) && news != null){
                            String image = xmlPullParser.nextText();
                            if(!TextUtils.isEmpty(image)){
                                image = image.replace("images0.cnblogs.com/news_topic///","");
                            }
                            news.image = image;
                        }else if("sourceName".equals(tag) && news != null){
                            news.source = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if("entry".equals(tag)){
                            newses.add(news);
                            news = null;
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
            return newses;
        }
    }
}
