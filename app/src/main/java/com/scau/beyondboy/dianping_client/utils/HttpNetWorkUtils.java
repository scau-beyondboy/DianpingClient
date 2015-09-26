package com.scau.beyondboy.dianping_client.utils;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-06
 * Time: 11:23
 */
public class HttpNetWorkUtils
{
    private static final String TAG = HttpNetWorkUtils.class.getName();
    private static OkHttpClient sClient=new OkHttpClient();

    /**
     * 同步发送Get方法请求获取字符串数据
     * @param  UriString 要请求资源的标识
     */
    public static String getResponseString(String UriString) throws IOException
    {
        Log.i(TAG,"getResponseString:  "+UriString );
        Request request=new Request
                .Builder()
                .url(UriString)
                .build();
        Response response=sClient.newCall(request).execute();
        if(!response.isSuccessful())throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    /**
     * 带参数同步发送Get{@link #getResponseString(String)},并返回String格式的数据
     * @param UriString 要请求资源的表示
     * @param param get方法参数
     */
    public static String getResponseStringtWithparam(String UriString, String param) throws IOException
    {
        return getResponseString(UriString + "?" + param);
    }

    /**
     * 异步发送Get方法请求资源并在客户端处理该资源
     * @param UriString 要请求资源的表示
     * @param callback 回调接口{@link Callback}
     */
    public static void Asyget(String UriString, Callback callback) throws IOException
    {
        Request request=new Request
                .Builder()
                .url(UriString)
                .build();
        sClient.newCall(request).enqueue(callback);
    }

    /**
     * 带参数异步发送Get{@link #Asyget(String, Callback)}
     * @param UriString 要请求资源的表示
     * @param callback 回调接口{@link Callback}
     * @param param get方法参数
     */
    public static void AsygetWithparam(String UriString,String param,Callback callback) throws IOException
    {
        Asyget(UriString + "?" + param, callback);
    }

    /*
    * @param url 图片网址
    * @return 返回图片字节数组
    * @throws IOException
    */
    public static byte[] getUrlBytes(String url) throws IOException
    {
        StringBuilder urlData=new StringBuilder();
        URL url1=new URL(url);
        // Log.i(TAG,"URL是多少：  "+url1.toString() );
        HttpURLConnection connection=(HttpURLConnection)url1.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        try
        {
            InputStream inputStream=connection.getInputStream();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK)
            {
                return null;
            }
            int byteRead=0;
            byte[] buffer=new byte[1024];
            while ((byteRead=inputStream.read(buffer,0,buffer.length))!=-1)
            {
                outputStream.write(buffer, 0, byteRead);
            }
            outputStream.close();
            //   Log.i(TAG, "拉取结果值：  " + outputStream.toString());
            return outputStream.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.disconnect();
        }
        return null;
    }

    /**
     * 开启线程异步拉取数据，同步等待返回结果
     * @param UriString 要请求资源的表示
     * @param param get方法参数
     * @param requestCallBack 回调接口
     */
    public static void synchroGetwithParam(String UriString,String param,RequestCallBack requestCallBack)
    {
        // 使用FutureTask来包装Callable对象
        FutureTask<String> task = new FutureTask<String>(new AsytaskNetWork(UriString,param));
        new Thread(task,"开启线程拉起网络").start();
        try
        {
         // Log.i(TAG, "开始同步拉起网络:  "+task.get());
            if(task.isDone())
                requestCallBack.onSuccess(task.get());
        } catch (Exception e)
        {
            e.printStackTrace();
            requestCallBack.onFailure(e,"拉取数据失败");
        }
    }

    /**
     * 异步网络拉取数据返回结果
     */
    private static class AsytaskNetWork implements Callable<String>
    {
        private String uriString;
        private String param;
        private AsytaskNetWork(String uriString, String param)
        {
            this.uriString = uriString;
            this.param = param;
        }
        // 实现call方法，作为线程执行体
        public String call() throws IOException
        {
            String result= null;
            result = getResponseStringtWithparam(uriString,param);
            //Log.i(TAG,"result:  "+result);
            return result;
        }
    }

    /**
     * 成功和失败拉取数据回调接口
     */
    public interface RequestCallBack<T>
    {
        void onSuccess(T result);
        void onFailure(Exception arg0, String arg1);
    }
}
