package com.jack.jianyu.ui.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.jack.jianyu.MyApplication;
import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.bean.StoriesEntity;
import com.jack.jianyu.bean.ZhihuContentBean;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.utils.GsonUtil;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.jack.jianyu.utils.NetWorkUtils;
import com.jack.jianyu.utils.ShareManager;
import com.jack.jianyu.utils.ToastHelper;
import com.jack.jianyu.utils.http.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * author:S.jack
 * data:2016-01-22 22:25
 */


public class NewContentActivity extends BaseActivity {

    @InjectView(R.id.iv_header)
    ImageView ivHeader;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @InjectView(R.id.webview_content)
    WebView webviewContent;

    @InjectView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    private StoriesEntity storiesEntity = null;

    private MenuItem mFavActionItem;

    private Menu mOptionsMenu;

    private boolean isInFavorite = false;

    private long newsId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content_layout);
        ButterKnife.inject(this);

        storiesEntity = (StoriesEntity) getIntent().getSerializableExtra("zhihuBean");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        collapsingToolbarLayout.setTitle(storiesEntity.getTitle());

        newsId = storiesEntity.getId();

        new FavoriteStatusGetTask().execute();

        setUpWebViewDefaults();
    }

    private void setUpWebViewDefaults() {

        //设置缓存模式
        webviewContent.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webviewContent.getSettings().setJavaScriptEnabled(true);

        // 开启DOM storage API 功能
        webviewContent.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webviewContent.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        webviewContent.getSettings().setAppCacheEnabled(true);
        if (NetWorkUtils.isNetworkConnected(this)) {
            HttpUtils.get(AppConstant.ZhiHuUrl.URL_DETAIL + storiesEntity.getId(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        parseJson(new String(bytes, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                }
            });
        }
    }

    private void parseJson(String responseString) {
        ZhihuContentBean zhihuContentBean = GsonUtil.getData(responseString, ZhihuContentBean.class);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(zhihuContentBean.getImage(), ivHeader, ImageLoaderHelper.getInstance(this).getDisplayOptions());
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + zhihuContentBean.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webviewContent.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCreateMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.activity_news_menu, menu);
        mFavActionItem = menu.findItem(R.id.menu_item_fav_action_bar);
        if (isInFavorite) {
            mFavActionItem.setIcon(R.mipmap.collected);
            mFavActionItem.setTitle("取消收藏");
        } else {
            mFavActionItem.setIcon(R.mipmap.collect);
            mFavActionItem.setTitle("收藏成功");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_fav_action_bar:
                if (isInFavorite) {
                    MyApplication.getNewsFavoriteDataSource().deleteFromFavorite(String.valueOf(newsId));
                    ToastHelper.showShort(this, "取消收藏");

                    mFavActionItem.setIcon(R.mipmap.collect);
                    mFavActionItem.setTitle("收藏");
                    isInFavorite = false;
                } else {
                    String title = null, image = null, share_url = null;
                    if (storiesEntity != null) {
                        title = storiesEntity.getTitle();
                        image = storiesEntity.getImages().get(0);
                        share_url = storiesEntity.getShare_url();
                    }

                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(image)) {
                        MyApplication.getNewsFavoriteDataSource().add2Favorite(String.valueOf(newsId), title,
                                image, share_url);

                        ToastHelper.showShort(this, "收藏成功");

                        mFavActionItem.setIcon(R.mipmap.collected);
                        mFavActionItem.setTitle("取消收藏");

                        isInFavorite = true;
                    } else {
                        ToastHelper.showShort(this, "收藏失败");
                    }
                }
                break;
            case R.id.menu_item_share_action_provider_action_bar:
                ShareManager.getInstance().popUmengShareDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FavoriteStatusGetTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            return MyApplication.getNewsFavoriteDataSource().isInFavorite(String.valueOf(newsId));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            isInFavorite = result;

            updateCreateMenu();
        }
    }

    @SuppressLint("NewApi")
    private void updateCreateMenu() {
        if (Build.VERSION.SDK_INT >= 11) {
            invalidateOptionsMenu();
        } else if (mOptionsMenu != null) {
            mOptionsMenu.clear();
            onCreateOptionsMenu(mOptionsMenu);
        }
    }
}
