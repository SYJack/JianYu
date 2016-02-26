package com.jack.jianyu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jack on 2016/2/3.
 */
public class BeforeBean implements Serializable {
    private List<StoriesEntity> stories;
    private String date;

    public List<StoriesEntity> getStories() {
        return stories;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
