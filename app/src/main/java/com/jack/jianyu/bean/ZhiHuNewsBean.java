package com.jack.jianyu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jack on 2016/1/29.
 */
public class ZhiHuNewsBean {
    private String date;

    private List<StoriesEntity> stories;

    private List<TopStoriesEntity> top_stories;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public List<StoriesEntity> getStories() {
        return this.stories;
    }

    public void setTop_stories(List<TopStoriesEntity> top_stories) {
        this.top_stories = top_stories;
    }

    public List<TopStoriesEntity> getTop_stories() {
        return this.top_stories;
    }

    public static class TopStoriesEntity implements Serializable {

        private String image;

        private int type;

        private int id;

        private String ga_prefix;

        private String title;

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return this.image;
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

        public int getId() {
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

    }


    @Override
    public String toString() {
        return "ZhiHuNewsBean{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}
