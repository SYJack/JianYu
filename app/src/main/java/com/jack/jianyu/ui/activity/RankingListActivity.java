package com.jack.jianyu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.bean.MusicContentListBean;
import com.jack.jianyu.bean.RankingListBean;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.ui.BooleanWifiDialog;
import com.jack.jianyu.ui.UIHelp;
import com.jack.jianyu.ui.adapter.RankingListViewAdapter;
import com.jack.jianyu.utils.DensityUtils;
import com.jack.jianyu.utils.GsonUtil;
import com.jack.jianyu.utils.NetWorkUtils;
import com.jack.jianyu.utils.ToastHelper;
import com.jack.jianyu.utils.animation.LoadingAnimation;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.show.api.ShowApiRequest;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * author:S.jack
 * data:2016-01-13 11:20
 */

public class RankingListActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.lv_ranginglist)
    ListView lvRanginglist;

    @InjectView(R.id.ll_error_layout)
    LinearLayout llErrorLayout;

    @InjectView(R.id.search_loading_img)
    ImageView searchLoadingImg;

    @InjectView(R.id.search_loading_layout)
    LinearLayout searchLoadingLayout;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private RankingListViewAdapter mRankingListViewAdapter;

    private List<MusicContentListBean> songlists = null;

    private ViewGroup mFooterView;
    private int type = -1;
    private int listPosition = -1;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //没有网络
                case 0:
                    llErrorLayout.setVisibility(View.VISIBLE);
                    llErrorLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llErrorLayout.setVisibility(View.GONE);
                            getRankingListMusic(type);
                        }
                    });
                    ToastHelper.showShort(RankingListActivity.this, R.string.str_error_networt);
                    break;
                case 1:
                    LoadingAnimation.closeLoadingAnimation(searchLoadingLayout);
                    initListview();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankinglist_layout);
        ButterKnife.inject(this);
        lvRanginglist.setOnItemClickListener(this);
        initToobar();
        initData();
    }

    private void initToobar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initData() {
        //获取类型代号
        int topid = getIntent().getIntExtra(AppConstant.RANKING_LIST_TYPE, 0);
        type = topid;
        getAppSecret(topid);
    }

    private void getAppSecret(int topid) {
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        String date = sDateFormat.format(new java.util.Date());
//        String secret = "showapi_appid" + AppConstant.MY_APPID + "showapi_timestamp" + date + "topid" + topid + AppConstant.MY_APPSECRET;
//        String sign = MD5Util.md5Hex(secret);
        getRankingListMusic(topid);
    }

    private void getRankingListMusic(int topid) {
        if (NetWorkUtils.isNetworkConnected(this)) {
            LoadingAnimation.showLoadingAnimation(searchLoadingLayout, searchLoadingImg);
            new ShowApiRequest(AppConstant.ShowAPIUrls.RANKING_SONG_URLS, AppConstant.ShowAPIUrls.MY_APPID, AppConstant.ShowAPIUrls.MY_APPSECRET)
                    .setResponseHandler(responseHandler)
                    .addTextPara("topid", topid + "")
                    .post();

        } else {
            handler.sendMessage(handler.obtainMessage(0));
        }
    }

    protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {

                RankingListBean rankingListBean = GsonUtil.getData(new String(bytes, "utf-8"), RankingListBean.class);
                songlists = rankingListBean.getShowapi_res_body().getPagebean().getSonglist();
                handler.sendMessage(handler.obtainMessage(1));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    };

    private void initListview() {
        View footerView = getFooterView();
        if (songlists != null) {
            if (lvRanginglist.getFooterViewsCount() == 0) {
                mRankingListViewAdapter = new RankingListViewAdapter(RankingListActivity.this, songlists);
                lvRanginglist.addFooterView(footerView);
                lvRanginglist.setAdapter(mRankingListViewAdapter);
            }
        }
    }

    private View getFooterView() {
        if (mFooterView == null) {
            FrameLayout fl = new FrameLayout(this);
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(0xFFaaaaaa);
            tv.setBackgroundColor(0xFFffffff);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , DensityUtils.dp2px(this, 36));
            fl.addView(tv, params);
            mFooterView = fl;
        }
        TextView tv = (TextView) mFooterView.getChildAt(0);
        tv.setText("共有" + songlists.size() + "首歌");
        return mFooterView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BooleanWifiDialog dialog = new BooleanWifiDialog(this);
        listPosition = position;
        MusicContentListBean rankingSonglist = (MusicContentListBean) parent.getAdapter().getItem(position);
        if (NetWorkUtils.isWifi(this)) {
            UIHelp.PlayMusic(this, rankingSonglist);
        } else {
            dialog.booleWifiDialog(rankingSonglist);
        }
    }

}
