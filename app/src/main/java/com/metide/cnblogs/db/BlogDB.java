package com.metide.cnblogs.db;

import android.database.Cursor;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.utils.SqliteHelper;

/**
 * Author   metide
 * Date     2017/4/21
 */

public class BlogDB {
    public synchronized static void save(BlogResult.Blog blog){
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
                    "comments) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)";

            SqliteHelper.exeSQL(sql,
                    blog.id,
                    blog.title,
                    blog.link,
                    blog.summary,
                    blog.published,
                    blog.updated,
                    blog.author.avatar,
                    blog.author.name,
                    blog.author.uri,
                    blog.diggs,
                    blog.views,
                    blog.comments
            );
        }
    }
}
