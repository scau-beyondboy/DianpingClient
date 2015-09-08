package com.scau.beyondboy.dianping_client.utils;

import android.content.Context;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-30
 * Time: 20:10
 */
public class ShareUtils
{

    public static final String FILE_NAME = "dianping";
    public static final String WELCOME_ENTER_FLAG = "welcomeEnterFlag";
    public static final String CITY_NAME = "cityName";

    public static boolean getWelcomeEnterFlag(Context context)
    {
        return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).getBoolean(WELCOME_ENTER_FLAG,false);
    }
    public static  void putWelcomeEnterFlag(Context context,boolean value)
    {
        context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).edit().putBoolean(WELCOME_ENTER_FLAG,value).apply();
    }

    public static void putCityName(Context context, String cityName)
    {
        context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).edit().putString(CITY_NAME, cityName).apply();
    }

    public static String getCityName(Context context)
    {
        return context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).getString(CITY_NAME, "选择城市");
    }
}
