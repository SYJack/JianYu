package com.jack.jianyu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jack.jianyu.MyApplication;
import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.bean.StoriesEntity;
import com.jack.jianyu.ui.adapter.NewsItemAdapter;
import com.jack.jianyu.utils.MyAsyncTask;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jack on 2016/2/26.
 */
public class FavoriteActivity extends BaseActivity {

    private static final int REQUESTCODE_DETAIL = 8010;

    @InjectView(R.id.news_correct_list)
    ListView mListView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private NewsItemAdapter mAdapter = null;

    private ArrayList<StoriesEntity> mEntities = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corrected);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StoriesEntity storiesEntity = mEntities != null ? mEntities.get(position) : null;

                if (storiesEntity == null) {
                    return;
                }
                Intent intent = new Intent(FavoriteActivity.this, NewContentActivity.class);
                intent.putExtra("zhihuBean", storiesEntity);
                startActivityForResult(intent, REQUESTCODE_DETAIL);
            }
        });

        new LoadCacheNewsTask().executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_clean_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clean_news:
                MyApplication.getNewsFavoriteDataSource().deleteFromFavorite();
                new LoadCacheNewsTask().executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_DETAIL) {
            new LoadCacheNewsTask().executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class LoadCacheNewsTask extends MyAsyncTask<String, Void, ArrayList<StoriesEntity>> {

        @Override
        protected ArrayList<StoriesEntity> doInBackground(String... params) {
            return MyApplication.getNewsFavoriteDataSource().getFavoriteList();
        }

        @Override
        protected void onPostExecute(ArrayList<StoriesEntity> result) {
            super.onPostExecute(result);

            if (result != null) {
                mEntities = result;
                if (mAdapter == null) {
                    mAdapter = new NewsItemAdapter(FavoriteActivity.this, mEntities);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.updateData(mEntities);
                }
            } else {
                mAdapter.updateData(mEntities);
            }
        }
    }
}
