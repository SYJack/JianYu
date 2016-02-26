package com.jack.jianyu.utils;

/**
 * author:S.jack
 * data:2016-01-13 17:45
 */


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jack.jianyu.MyApplication;

import java.io.File;

/***
 * 常用小工具类
 */
public class CommonUtils {


    private static int[] sScreenSize;


    /**
     * 获取屏幕size
     */
    public static int[] getScreenSize(Context context) {
        if (sScreenSize == null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            sScreenSize = new int[]{metrics.widthPixels, metrics.heightPixels};
        }
        return sScreenSize;
    }

    /**
     * 获取文件的后缀名，返回大写
     */
    public static String getSuffix(String str) {
        int i = str.lastIndexOf('.');
        if (i != -1) {
            return str.substring(i + 1).toUpperCase();
        }
        return str;
    }

    /**
     * 格式化时间，将毫秒转换为分:秒格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static String durationToString(long duration) {
        int secondAll = (int) (duration / 1000);
        int minute = secondAll / 60;
        int second = secondAll % 60;
        return String.format("%02d:%02d ", minute, second);
    }

    /**
     * 格式化文件大小 Byte->MB
     */
    public static String formatByteToMB(long l) {
        float mb = l / 1024f / 1024f;
        return String.format("%.2f", mb);
    }

    public static void hideSoftKeyboard(View view) {
        if (view == null)
            return;
        ((InputMethodManager) MyApplication.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    /**
     * 分享功能
     */
    public static void shareMsg(Context context, String activityTitle, String msgTitle, String msgText,
                                String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/png");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, activityTitle));
    }


}
