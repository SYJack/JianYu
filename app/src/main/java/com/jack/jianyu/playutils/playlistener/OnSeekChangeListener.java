package com.jack.jianyu.playutils.playlistener;

/**
 * 进度条监听接口
 * Created by jack on 2016/2/23.
 */
public interface OnSeekChangeListener {
    /**
     * @param progress 播放的进度
     * @param max      最大值
     * @param time     当前播放的时间
     * @param duration 总时间
     */
    public void onSeekChange(int progress, int max, String time, String duration);

}
