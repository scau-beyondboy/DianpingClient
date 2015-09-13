package com.scau.beyondboy.dianping_client.utils;

import com.scau.beyondboy.dianping_client.Consts.Consts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-09-06
 * Time: 17:39
 */
@RunWith(JUnit4.class)
public class HttpNetWorkUtilsTest
{

    @Test
    public void testGet() throws Exception
    {
        System.out.println(HttpNetWorkUtils.getResponseString(Consts.HOST + Consts.CITY_DATA));
    }
}