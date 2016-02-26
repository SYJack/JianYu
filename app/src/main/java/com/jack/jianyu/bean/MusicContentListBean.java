package com.jack.jianyu.bean;

import java.io.Serializable;

/**
 * Created by jack on 2016/2/12.
 */
public class MusicContentListBean implements Serializable {
    private int albumid;

    private int seconds;

    private String url;

    private String albummid;

    private String albumname;

    private String albumpic_big;

    private String albumpic_small;

    private String downUrl;

    private String m4a;

    private String media_mid;

    private int singerid;

    private String singername;

    private int songid;

    private String songmid;

    private String songname;

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public int getAlbumid() {
        return this.albumid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getAlbummid() {
        return this.albummid;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumname() {
        return this.albumname;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    public String getAlbumpic_big() {
        return this.albumpic_big;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
    }

    public String getAlbumpic_small() {
        return this.albumpic_small;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getDownUrl() {
        return this.downUrl;
    }

    public void setM4a(String m4a) {
        this.m4a = m4a;
    }

    public String getM4a() {
        return this.m4a;
    }

    public void setMedia_mid(String media_mid) {
        this.media_mid = media_mid;
    }

    public String getMedia_mid() {
        return this.media_mid;
    }

    public void setSingerid(int singerid) {
        this.singerid = singerid;
    }

    public int getSingerid() {
        return this.singerid;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getSingername() {
        return this.singername;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public int getSongid() {
        return this.songid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getSongmid() {
        return this.songmid;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongname() {
        return this.songname;
    }
}
