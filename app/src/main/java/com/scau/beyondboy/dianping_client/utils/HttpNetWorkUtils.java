package com.scau.beyondboy.dianping_client.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-06
 * Time: 11:23
 */
public class HttpNetWorkUtils
{
    private static OkHttpClient sClient=new OkHttpClient();

    /**
     * 同步发送Get方法请求获取字符串数据
     * @param  UriString 要请求资源的标识
     */
    public static String get(String UriString) throws IOException
    {
        Request request=new Request
                .Builder()
                .url(UriString)
                .build();
        Response response=sClient.newCall(request).execute();
        if(!response.isSuccessful())throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    /**
     * 异步发送Get方法请求资源并在客户端处理该资源
     * @param UriString 要请求资源的表示
     * @param callback 回调接口{@link Callback}
     */
    public static void get(String UriString,Callback callback) throws IOException
    {
        Request request=new Request
                .Builder()
                .url(UriString)
                .build();
        sClient.newCall(request).enqueue(callback);
    }
}
