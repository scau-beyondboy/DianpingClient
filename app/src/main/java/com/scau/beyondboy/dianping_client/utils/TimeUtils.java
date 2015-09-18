/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-07-11
 * Time: 11:26
 */
package com.scau.beyondboy.dianping_client.utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * 转换时间的工具类
 */
public final class TimeUtils
{
    private static final String TAG = TimeUtils.class.getName();
    private TimeUtils()
    {

    }
    /**
     * 将字符型的时间表示转换成long型
     * @param time 用户发表微博的时间
     * @return long型的时间
     * @throws ParseException 解析异常
     */
    public static long getLongByGMT(String time) throws ParseException
    {
        // Wed Jul 15 09:02:35 +0800 2015",
        DateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        return (format.parse(time).getTime()/1000);
    }
    /**
     * 获得时间差的字符内容
     * @param timestamp 用户发表微博的时间
     */
    public static String converTime(long timestamp)
    {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 24 * 60 * 60)
        {
            // 1天以上
            timeStr = timeGap / (24 * 60 * 60) + "天前";
        } else if (timeGap > 60 * 60)
        {
            // 1小时-24小时
            timeStr = timeGap / (60 * 60) + "小时前";
        } else if (timeGap > 60)
        {
            // 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        } else
        {
            // 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }
}
