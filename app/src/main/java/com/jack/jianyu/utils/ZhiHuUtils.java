package com.jack.jianyu.utils;

import android.content.Context;

import com.jack.jianyu.R;
import com.jack.jianyu.utils.http.DateUtils;

import java.util.Date;

/**
 * Created by jack on 2016/1/31.
 */
public class ZhiHuUtils {
    /**
     * 获取listview item 日期
     *
     * @param dateStr
     * @return
     */
    public static String getDateTag(Context context, String dateStr) {

        String currentDate = DateUtils.getCurrentDate(DateUtils.MMDD);

        String pre = DateUtils.getFormatTime(dateStr, DateUtils.YYYYMMDD, DateUtils.MMDD);

        Date date = DateUtils.getFormatTimeDate(dateStr, DateUtils.YYYYMMDD, DateUtils.MMDD);

        String week = DateUtils.getWeekOfDate(date);

        return currentDate.equals(pre) ? context.getString(R.string.listview_hotnews) : new StringBuilder().append(pre).append(" ").append(week).toString();
    }
}
