package com.jack.jianyu.playutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jack.jianyu.bean.MusicContentListBean;
import com.jack.jianyu.utils.LogUtils;

/**
 * Created by jack on 2016/2/23.
 */
public class MusicPlayerReceiver extends BroadcastReceiver {

    // 点击播放列表时的action
    public static final String ACTION_PLAY_ITEM = "com.jack.jianyu.playutils.ACTION_PLAY_ITEM";
    // 点击通知后跳转到当前应用的action
    public static final String ACTION_BACKUP_ZERO = "com.jack.jianyu.playutils.ACTION_BACKUP_ZERO";
    // 点击了播放/暂停键的时候发这个action
    public static final String ACTION_PLAY_BUTTON = "com.jack.jianyu.playutils.ACTION_PLAY_BUTTON";
    // 上一首action
    public static final String ACTION_PLAY_PREVIOUS = "com.jack.jianyu.playutils.ACTION_PLAY_PREVIOUS";
    // 下一首action
    public static final String ACTION_PLAY_NEXT = "com.jack.jianyu.playutils.ACTION_PLAY_NEXT";
    // 播放模式的action
    public static final String ACTION_PLAY_MODE = "com.jack.jianyu.playutils.ACTION_PLAY_MODE";
    // seekbar进度更改的action
    public static final String ACTION_SEEKBAR = "com.jack.jianyu.playutils.ACTION_SEEKBAR";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.d("ACTION-----" + action);
        // 如果收到的是点击播放列表时发送的广播
        if (action.equals(ACTION_PLAY_ITEM)) {
            MusicPlayService.mSonginfo = (MusicContentListBean) intent.getSerializableExtra("MusicInfoBean");
            // state改变为play，播放歌曲
            MusicPlayService.state = MusicPlayState.MPS_PLAYING;
            // 如果state改变
            MusicPlayService.stateChange = true;
        }
        // 如果接收的是点击暂停/播放键时的广播
        if (action.equals(ACTION_PLAY_BUTTON)) {
            if (MusicPlayService.mSonginfo != null) {

                // 根据当前状态点击后，进行相应状态改变
                switch (MusicPlayService.state) {
                    case MusicPlayState.MPS_PLAYING:
                    case MusicPlayState.MPS_CONTINUE:
                        MusicPlayService.state = MusicPlayState.MPS_PAUSE;
                        break;
                    case MusicPlayState.MPS_PAUSE:
                        MusicPlayService.state = MusicPlayState.MPS_CONTINUE;
                        break;
                }
                // state改变
                MusicPlayService.stateChange = true;
            }
        }
        if (action.equals(ACTION_SEEKBAR)) {
            // 得到传过来的当前进度条进度，更改歌曲播放位置
            int progress = intent.getIntExtra(MusicPlayState.ACTION_SEEK_TO, 0);
            int state = intent.getIntExtra(MusicPlayState.PLAYER_STATE, 0);
            MusicPlayService.playerSeekToTime(progress, state);
            // 进度条改变
            MusicPlayService.seekChange = true;
        }
    }
}
