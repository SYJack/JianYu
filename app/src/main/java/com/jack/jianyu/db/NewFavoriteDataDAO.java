package com.jack.jianyu.db;

import com.jack.jianyu.bean.StoriesEntity;

import java.util.ArrayList;

/**
 * 数据库访问接口
 * Created by jack on 2016/2/26.
 */
public interface NewFavoriteDataDAO {
    /**
     * 添加news
     */
    public void insert(String newsId, String newsTitle, String newsLogo, String newsShareUrl);

    /**
     * 收藏
     */
    public boolean add2Favorite(String newsId, String newsTitle, String newsLogo, String newsShareUrl);

    /**
     * 新闻是否已经收藏
     **/
    public boolean isInFavorite(String newsId);

    /**
     * 获取收藏列表
     */
    public ArrayList<StoriesEntity> getFavoriteList();

    /**
     * 通过newsId删除收藏夹的文章
     */
    public void deleteFromFavorite(String newsId);

    public void deleteFromFavorite();
}

