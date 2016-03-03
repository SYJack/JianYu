package com.jack.jianyu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jack.jianyu.bean.StoriesEntity;

import java.util.ArrayList;

/**
 * Created by jack on 2016/2/26.
 */
public class NewsFavoriteDataSource implements NewFavoriteDataDAO {

    private SQLiteDatabase database;

    private String[] allColumns = {
            DatabaseHelper.FAVORITE_COLUMN_ID,
            DatabaseHelper.FAVORITE_COLUMN_NEWS_ID,
            DatabaseHelper.FAVORITE_COLUMN_NEWS_TITLE,
            DatabaseHelper.FAVORITE_COLUMN_NEWS_LOGO,
            DatabaseHelper.FAVORITE_COLUMN_NEWS_SHARE_URL
    };

    public NewsFavoriteDataSource(DatabaseHelper databaseHelper) {
        database = databaseHelper.getWritableDatabase();
    }

    @Override
    public void insert(String newsId, String newsTitle, String newsLogo, String newsShareUrl) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.FAVORITE_COLUMN_NEWS_ID, newsId);
        values.put(DatabaseHelper.FAVORITE_COLUMN_NEWS_TITLE, newsTitle);
        values.put(DatabaseHelper.FAVORITE_COLUMN_NEWS_LOGO, newsLogo);
        values.put(DatabaseHelper.FAVORITE_COLUMN_NEWS_SHARE_URL, newsShareUrl);

        database.insert(DatabaseHelper.FAVORITE_TABLE_NAME, null, values);
    }

    @Override
    public boolean add2Favorite(String newsId, String newsTitle, String newsLogo, String newsShareUrl) {
        if (TextUtils.isEmpty(newsId)) {
            return false;
        }
        if (!isInFavorite(newsId)) {
            insert(newsId, newsTitle, newsLogo, newsShareUrl);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInFavorite(String newsId) {
        boolean result = false;
        Cursor cursor = database.query(DatabaseHelper.FAVORITE_TABLE_NAME, allColumns,
                DatabaseHelper.FAVORITE_COLUMN_NEWS_ID + "= '" + newsId + "'", null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        return result;
    }

    @Override
    public ArrayList<StoriesEntity> getFavoriteList() {

        ArrayList<StoriesEntity> newsList = new ArrayList<StoriesEntity>();

        String orderBy = DatabaseHelper.FAVORITE_COLUMN_ID + " DESC";

        Cursor cursor = database.query(DatabaseHelper.FAVORITE_TABLE_NAME, allColumns,
                null, null, null, null, orderBy);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                StoriesEntity newsEntity = new StoriesEntity();

                String newsId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAVORITE_COLUMN_NEWS_ID));
                String newsTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAVORITE_COLUMN_NEWS_TITLE));
                String newsLogo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAVORITE_COLUMN_NEWS_LOGO));
                String newsShareUrl = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAVORITE_COLUMN_NEWS_SHARE_URL));

                newsEntity.setId(Integer.parseInt(newsId));
                newsEntity.setTitle(newsTitle);

                if (!TextUtils.isEmpty(newsLogo)) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(newsLogo);
                    newsEntity.setImages(list);
                }

                newsEntity.setShare_url(newsShareUrl);

                newsList.add(newsEntity);

                cursor.moveToNext();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return newsList;
    }

    @Override
    public void deleteFromFavorite(String newsId) {
        String whereClause = DatabaseHelper.FAVORITE_COLUMN_NEWS_ID + "=?";

        String[] whereArgs = {String.valueOf(newsId)};

        database.delete(DatabaseHelper.FAVORITE_TABLE_NAME, whereClause, whereArgs);
    }

    @Override
    public void deleteFromFavorite() {
        database.delete(DatabaseHelper.FAVORITE_TABLE_NAME, null, null);
    }
}
