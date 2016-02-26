package com.jack.jianyu.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.bean.StoriesEntity;
import com.jack.jianyu.bean.ZhihuContentBean;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.utils.GsonUtil;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.jack.jianyu.utils.NetWorkUtils;
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
        collapsingToolbarLayout.setTitle(storiesEntity.getTitle());
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


}
