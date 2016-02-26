package com.jack.jianyu.bean;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by jack on 2016/2/6.
 */
public class ChatMessageBean {

    //聊天内容
    private String msg;

    //日期
    private Date date;

    //头像
    private Bitmap roundImageView;


    private boolean isComMeg = true;

    public ChatMessageBean() {
    }

    public ChatMessageBean(String msg, Date date,Bitmap imageView, boolean isComMeg) {
        this.msg = msg;
        this.date = date;
        this.isComMeg = isComMeg;
        this.roundImageView=imageView;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Bitmap getRoundImageView() {
        return roundImageView;
    }

    public void setRoundImageView(Bitmap roundImageView) {
        this.roundImageView = roundImageView;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }
}
