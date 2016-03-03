package com.jack.jianyu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jack on 2016/2/2.
 */
public class StoriesEntity implements Serializable {

    private List<String> images;

    private int type;

    private long id;

    private String ga_prefix;

    private String title;

    private String share_url;

    private String date;

    //是否仅仅是个tag
    public boolean isTag = true;

    public boolean getIsTag() {
        return isTag;
    }

    public void setIsTag(boolean isTag) {
        this.isTag = isTag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getGa_prefix() {
        return this.ga_prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
