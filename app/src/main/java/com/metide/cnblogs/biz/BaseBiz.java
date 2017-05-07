package com.metide.cnblogs.biz;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author   metide
 * Date     2017/4/15
 */

public class BaseBiz {
    public static InputStream getInputStream(String url){
        URL u= null;
        HttpURLConnection connection= null;
        InputStream is = null;
        try {
            u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            //设置请求连接超时的时间（优化）
            connection.setConnectTimeout(5000);

            int code=connection.getResponseCode();
            Log.d("code", code + "");
            if(code==200){
                //获取服务器返回过来的结果
                is=connection.getInputStream();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return is;
    }
}
