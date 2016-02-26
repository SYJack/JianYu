package com.jack.jianyu.playutils.playlistener;

/**
 * 播放在线歌曲 缓冲更新的监听器
 * Created by jack on 2016/2/23.
 */
public interface OnBufferingUpdateListener {
    /**
     * 缓冲更新回调
     *
     * @param percent 缓冲的百分比
     */
    public void onBufferingUpdate(int percent);
}
