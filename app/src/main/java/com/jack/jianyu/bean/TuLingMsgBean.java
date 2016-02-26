package com.jack.jianyu.bean;

/**
 * Created by jack on 2016/2/7.
 */
public class TuLingMsgBean {
    private int code;
    private String text;
    private String url;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTulingResult() {
        return text;
    }

    public void setTulingResult(String tulingResult) {
        text = tulingResult;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
