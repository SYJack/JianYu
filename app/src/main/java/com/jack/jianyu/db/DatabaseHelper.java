package com.jack.jianyu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jack on 2016/2/25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //db
    public static final String DB_NAME = "news_paper.db";
    public static final int DB_VERSION = 1;

    //news_favorite
    public static final String FAVORITE_TABLE_NAME = "news_favorite";
    public static final String FAVORITE_COLUMN_ID = "_id";
    public static final String FAVORITE_COLUMN_NEWS_ID = "news_id";
    public static final String FAVORITE_COLUMN_NEWS_TITLE = "news_title";
    public static final String FAVORITE_COLUMN_NEWS_LOGO = "news_logo";
    public static final String FAVORITE_COLUMN_NEWS_SHARE_URL = "news_share_url";

    private static final String FAVORITE_TABLE_CREATE = "CREATE TABLE " + FAVORITE_TABLE_NAME
            + "(" + FAVORITE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FAVORITE_COLUMN_NEWS_ID + " CHAR(256) UNIQUE, "
            + FAVORITE_COLUMN_NEWS_TITLE + " CHAR(1024), "
            + FAVORITE_COLUMN_NEWS_LOGO + " CHAR(1024), "
            + FAVORITE_COLUMN_NEWS_SHARE_URL + " CHAR(1024));";

    private volatile static DatabaseHelper mHelper;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mHelper == null) {
            synchronized (DatabaseHelper.class) {
                mHelper = new DatabaseHelper(context);
            }
        }
        return mHelper;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAVORITE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + FAVORITE_TABLE_NAME);
    }
}
