package com.jack.jianyu.playutils.playlistener;

import com.jack.jianyu.bean.MusicContentListBean;

/**
 * Created by jack on 2016/2/23.
 */
public interface OnPlayerStateChangeListener {
    public void onStateChange(int state,MusicContentListBean musicContentListBean);
}
