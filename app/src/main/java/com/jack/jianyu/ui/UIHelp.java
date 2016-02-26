package com.jack.jianyu.ui;

import android.content.Context;
import android.content.Intent;

import com.jack.jianyu.bean.MusicContentListBean;
import com.jack.jianyu.playutils.MusicPlayerReceiver;

/**
 * 界面帮助类
 * author:S.jack
 * data:2016-01-18 17:58
 */


public class UIHelp {
    /**
     * 播放音乐
     */
    public static void PlayMusic(Context mContext, MusicContentListBean musicContentListBean) {
        Intent intent = new Intent();
        intent.setAction(MusicPlayerReceiver.ACTION_PLAY_ITEM);
        intent.putExtra("MusicInfoBean", musicContentListBean);
        mContext.sendBroadcast(intent);
    }
}
