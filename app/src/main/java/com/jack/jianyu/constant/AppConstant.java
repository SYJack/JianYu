package com.jack.jianyu.constant;

import android.os.Environment;

/**
 * author:S.jack
 * data:2016-01-12 15:57
 */


public class AppConstant {

    public static final String DESCRIPTOR = "com.umeng.share";
    public static final String RANKING_LIST_TYPE = "ranking_list_type"; //排行榜的类型，用于参数
    public static final String RANKING_LIST_NAME = "ranking_list_name"; //排行榜的名称，用于参数
    public static final String RANKING_LIST_RESID = "ranking_list_resID"; //排行榜的图片资源ID，用于参数

    public static final String APP_SHARE_URL = "http://www.wandoujia.com/apps/com.jack.jianyu";

    public static final class ShowAPIUrls {
        public static final String MY_APPID = "15010";
        public static final String MY_APPSECRET = "f7cf022408614c8dab5ede7f28f6e371";
        public static final String LIST_SONG_URLS = "http://route.showapi.com/213-1";
        public static final String RANKING_SONG_URLS = "http://route.showapi.com/213-4";
    }

    public static final class Paths {
        public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        public static final String IMAGE_LOADER_CACHE_PATH = "/JianYu/Images/";
        public static final String USER_IMAGE_PHOTO = "/JianYu/Photo/";
    }

    public static final class ZhiHuUrl {

        // 获取最新新闻
        public static final String URL_LATEST = "http://news-at.zhihu.com/api/4/news/latest";

        // 获取新闻详情
        public static final String URL_DETAIL = "http://news-at.zhihu.com/api/4/news/";

        // 获取过往新闻
        public static final String URLDEFORE = "http://news.at.zhihu.com/api/4/news/before/";
    }

    public static final class TuLingUrl {
        public static final String KEY_TULING = "48d9726d13a97c105c5e389abfcb8c86";
        public static final String URL_TULING = "http://www.tuling123.com/openapi/api";
    }
}
