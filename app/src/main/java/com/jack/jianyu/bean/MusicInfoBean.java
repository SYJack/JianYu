package com.jack.jianyu.bean;

import java.io.Serializable;

/**
 * Created by jack on 2016/2/16.
 */
public class MusicInfoBean implements Serializable{

    String singerPic;
    String songName;
    String singerName;
    String url;

    public String getSingerPic() {
        return singerPic;
    }

    public void setSingerPic(String singerPic) {
        this.singerPic = singerPic;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
