package com.metide.cnblogs.db;

import android.database.Cursor;

import com.metide.cnblogs.bean.Blog;

/**
 * Author   metide
 * Date     2017/4/21
 */

public class BlogDB {

    public static void save(Blog blog){
        String sql = "select id from blog where id=?";

        Cursor cursor = SqliteHelper.rawQuery(sql, blog.id);

        if(cursor != null && cursor.moveToNext()){
            cursor.close();
        }else{
            sql = "insert into blog(" +
                    "id," +
                    "title," +
                    "link," +
                    "summary," +
                    "authorImage," +
                    "authorName," +
                    "authorLink,time," +
                    "comment," +
                    "view," +
                    "recommendation) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?)";

            SqliteHelper.exeSQL(sql,
                    blog.id,
                    blog.title,
                    blog.link,
                    blog.summary,
                    blog.authorImage,
                    blog.authorName,
                    blog.authorLink,
                    blog.time,
                    blog.comment,
                    blog.view,
                    blog.recommendation
            );
        }
    }
}
