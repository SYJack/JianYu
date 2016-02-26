package com.jack.jianyu.utils.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 对android-async-http的一些封装
 * author:S.jack
 * data:2016-01-14 16:10
 */


public class HttpUtils {
    private static AsyncHttpClient httpClient = new AsyncHttpClient();


    public static AsyncHttpClient getClient() {
        return httpClient;
    }

    static {
        httpClient.setTimeout(6 * 1000);
    }

    public static void get(String urlString, AsyncHttpResponseHandler res)    //用一个完整url获取一个string对象

    {
        httpClient.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res)   //url里面带参数
    {
        httpClient.get(urlString, params, res);
    }

    public static void get(String urlString, JsonHttpResponseHandler res)   //不带参数，获取json对象或者数组
    {
        httpClient.get(urlString, res);
    }

    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res)   //带参数，获取json对象或者数组
    {
        httpClient.get(urlString, params, res);
    }

    public static void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
    {
        httpClient.get(uString, bHandler);
    }

    public static void post(String uString, RequestParams params, JsonHttpResponseHandler bHandler)   //post数据使用，会返回json数据
    {
        httpClient.post(uString, params, bHandler);
    }

    public static void post(String uString, RequestParams params, AsyncHttpResponseHandler res)  //post数据使用，返回普通的手
    {
        httpClient.post(uString, params, res);
    }
}
