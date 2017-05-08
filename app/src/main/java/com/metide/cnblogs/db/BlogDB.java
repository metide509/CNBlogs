package com.metide.cnblogs.db;

import android.database.Cursor;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.utils.DateUtil;
import com.metide.cnblogs.utils.SqliteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/21
 */

public class BlogDB {

    /**
     * 保存博客列表
     * @param blog
     */
    public synchronized static void save(BlogResult.Blog blog, int category){
        String sql = "select id from blog where id=?";

        Cursor cursor = SqliteHelper.rawQuery(sql, blog.id);

        if(cursor != null && cursor.moveToNext()){
            cursor.close();
        }else{
            sql = "insert into blog(" +
                    "id," +
                    "title," +
                    "summary," +
                    "published," +
                    "updated," +
                    "avatar," +
                    "name," +
                    "uri," +
                    "link," +
                    "diggs," +
                    "views," +
                    "comments," +
                    "category) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            SqliteHelper.exeSQL(sql,
                    blog.id,
                    blog.title,
                    blog.summary,
                    DateUtil.toUnix(blog.published, "yyyy-MM-dd'T'HH:mm:ss+08:00"),
                    DateUtil.toUnix(blog.updated, "yyyy-MM-dd'T'HH:mm:ss'Z'"),
                    blog.author.avatar,
                    blog.author.name,
                    blog.author.uri,
                    blog.link.href,
                    blog.diggs,
                    blog.views,
                    blog.comments,
                    category
            );
        }
    }


    public static List<BlogResult.Blog> getBlogsByCategory(int category, int pageIndex, int pageSize){
        String sql = "select id,title,summary,published,updated,avatar,name,uri,link,diggs,views,comments from blog where category=? order by published desc limit ? offset ?";

        Cursor cursor = SqliteHelper.rawQuery(sql, Integer.toString(category), Integer.toString(pageIndex), Integer.toString((pageIndex-1) * pageSize));
        List<BlogResult.Blog> blogs = new ArrayList<>();
        if(cursor != null){

            while (cursor.moveToNext()){
                BlogResult.Blog blog = new BlogResult.Blog();
                blog.id = cursor.getString(0);
                blog.title = cursor.getString(1);
                blog.summary = cursor.getString(2);
                blog.published = DateUtil.toString(cursor.getLong(3),"M-dd HH:mm");
                blog.updated = DateUtil.toString(cursor.getLong(4),"M-dd HH:mm");
                blog.author = new BlogResult.Author();
                blog.author.avatar = cursor.getString(5);
                blog.author.name = cursor.getString(6);
                blog.author.uri = cursor.getString(7);
                blog.diggs = cursor.getInt(8);
                blog.views = cursor.getInt(9);
                blog.comments = cursor.getInt(10);

                blogs.add(blog);
            }
            cursor.close();
        }

        return blogs;
    }
}
