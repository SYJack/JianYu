package com.jack.jianyu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseFragment;
import com.jack.jianyu.bean.MusicContentListBean;
import com.jack.jianyu.bean.MusicSongListBean;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.ui.BooleanWifiDialog;
import com.jack.jianyu.ui.UIHelp;
import com.jack.jianyu.ui.activity.MainActivity;
import com.jack.jianyu.ui.activity.RankingListActivity;
import com.jack.jianyu.ui.adapter.MusicSongListAdapter;
import com.jack.jianyu.ui.adapter.SearchGridViewAdapter;
import com.jack.jianyu.utils.CommonUtils;
import com.jack.jianyu.utils.GsonUtil;
import com.jack.jianyu.utils.NetWorkUtils;
import com.jack.jianyu.utils.ToastHelper;
import com.jack.jianyu.utils.animation.LoadingAnimation;
import com.jack.jianyu.widget.DisabledScrollGridView;
import com.jack.jianyu.widget.DisabledScrollListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.show.api.ShowApiRequest;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * author:S.jack
 * data:2016-01-13 10:52
 */


public class MusicFragment extends BaseFragment implements View.OnClickListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.ib_search_imgbtn)
    ImageButton ibSearchImgbtn;//搜索按钮

    @InjectView(R.id.et_search_content_edittext)
    EditText etSearchContentEdittext;//搜索框

    @InjectView(R.id.rl_search_layout)
    RelativeLayout rlSearchLayout;

    @InjectView(R.id.search_loading_img)
    ImageView searchLoadingImg;//加载时的动画

    @InjectView(R.id.search_loading_layout)
    LinearLayout searchLoadingLayout;//加载时的布局

    @InjectView(R.id.gv_search_rangking_gridview)
    DisabledScrollGridView gvSearchRangkingGridview;//排行榜的表格展示
    private SearchGridViewAdapter mGridViewAdapter;//排行榜Adapter

    @InjectView(R.id.ll_error_layout)
    LinearLayout llErrorLayout;//网络出错的页面

    @InjectView(R.id.search_result_layout)
    LinearLayout searchResultLayout;

    @InjectView(R.id.lv_search_result_song)
    DisabledScrollListView lvSearchResultSong;//搜索结果列表

    private List<MusicContentListBean> mSongContentlists = new ArrayList<>();
    private MusicSongListAdapter musicSongListAdapter = null;//歌曲列表的适配器

    private GestureDetector detector = null;
    private MainActivity.MyTouchListener myTouchListener = null;

    private String inputSongName = null;
    private int currentPages = 1;
    private int totalPages = 0;

    private int listPosition = -1;

    private boolean isLoading = false;


    // 榜单名称对应资源ID
    private int[] rankingListName = {R.string.str_rankinglist_oumeisong, R.string.str_rankinglist_neidisong,
            R.string.str_rankinglist_gangtaisong, R.string.str_rankinglist_hanguosong, R.string.str_rankinglist_ribensong,
            R.string.str_rankinglist_locksong, R.string.str_rankinglist_salesong, R.string.str_rankinglist_hotsong};
    // 榜单对应的图标对应资源ID
    private int[] rankingListIcon = {R.mipmap.ic_europeandamerica, R.mipmap.ic_china_lnland,
            R.mipmap.ic_hongkongandtaiwan, R.mipmap.ic_hanguo,
            R.mipmap.ic_japan, R.mipmap.ic_rock,
            R.mipmap.ic_sales_volume, R.mipmap.ic_hotsong};

    // 榜单对应的请求参数
    private int[] rankingListType = {3, 5, 6, 16, 17, 19, 23, 26};


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //没有网络
                case 0:
                    LoadingAnimation.closeLoadingAnimation(searchLoadingLayout);
                    llErrorLayout.setVisibility(View.VISIBLE);
                    llErrorLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llErrorLayout.setVisibility(View.GONE);
                            searchMusic();
                        }
                    });
                    ToastHelper.showShort(getActivity(), R.string.str_error_networt);
                    break;
                case 1:
                    LoadingAnimation.closeLoadingAnimation(searchLoadingLayout);
                    initListView();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.inject(this, view);
        registerSlide();
        getData();
        return view;
    }


    private void getData() {
        if (mGridViewAdapter == null) {
            mGridViewAdapter = new SearchGridViewAdapter(getActivity(), getDataResource());
            gvSearchRangkingGridview.setAdapter(mGridViewAdapter);
        }
        gvSearchRangkingGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RankingListActivity.class);
                intent.putExtra(AppConstant.RANKING_LIST_TYPE, rankingListType[position]);
                startActivity(intent);
            }
        });
        ibSearchImgbtn.setOnClickListener(this);
        lvSearchResultSong.setOnScrollListener(this);
        lvSearchResultSong.setOnItemClickListener(this);
    }

    private ArrayList<HashMap<String, Object>> getDataResource() {
        ArrayList<HashMap<String, Object>> dataSource = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(AppConstant.RANKING_LIST_NAME, rankingListName[i]);
            map.put(AppConstant.RANKING_LIST_RESID, rankingListIcon[i]);
            dataSource.add(map);
        }
        return dataSource;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search_imgbtn:
                CommonUtils.hideSoftKeyboard(etSearchContentEdittext);
                gvSearchRangkingGridview.setVisibility(View.GONE);
                lvSearchResultSong.setVisibility(View.GONE);
                currentPages = 1;
                mSongContentlists.clear();
                searchMusic();
                break;
        }
    }

    private void initListView() {
        if (mSongContentlists != null) {
            if (musicSongListAdapter == null) {
                musicSongListAdapter = new MusicSongListAdapter(getActivity(), mSongContentlists);
                lvSearchResultSong.setAdapter(musicSongListAdapter);
            }
            musicSongListAdapter.notifyDataSetChanged();
            lvSearchResultSong.setVisibility(View.VISIBLE);
        }
    }

    private void searchMusic() {
        inputSongName = etSearchContentEdittext.getText().toString();
        if (inputSongName.length() > 0) {
            if (NetWorkUtils.isNetworkConnected(getActivity())) {
                LoadingAnimation.showLoadingAnimation(searchLoadingLayout, searchLoadingImg);
                new ShowApiRequest(AppConstant.ShowAPIUrls.LIST_SONG_URLS, AppConstant.ShowAPIUrls.MY_APPID,
                        AppConstant.ShowAPIUrls.MY_APPSECRET)
                        .setResponseHandler(responseHandler)
                        .addTextPara("keyword", inputSongName)
                        .addTextPara("page", currentPages + "")
                        .post();
            } else {
                handler.sendMessage(handler.obtainMessage(0));
            }
        }
    }

    private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                MusicSongListBean listBean = GsonUtil.getData(new String(bytes, "utf-8"), MusicSongListBean.class);

                List<MusicContentListBean> mSongContentlist = listBean.getShowapi_res_body().getPagebean().getContentlist();

                mSongContentlists.addAll(mSongContentlist);

                /**必须要在这里设置，否则要点击两次才能显示数据*/
                handler.sendMessage(handler.obtainMessage(1));

                currentPages = listBean.getShowapi_res_body().getPagebean().getCurrentPage();
                totalPages = listBean.getShowapi_res_body().getPagebean().getAllPages();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (lvSearchResultSong != null && lvSearchResultSong.getChildCount() > 0) {
            if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                loadMore();
            }
        }
    }

    private void loadMore() {
        isLoading = true;
        currentPages = currentPages + 1;
        Log.e("pages", currentPages + "" + totalPages);
        if (currentPages < totalPages) {
            searchMusic();
        } else {
            isLoading = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BooleanWifiDialog dialog = new BooleanWifiDialog(getActivity());
        listPosition = position;
        MusicContentListBean contentListBean = (MusicContentListBean) parent.getAdapter().getItem(position);
        if (NetWorkUtils.isWifi(getActivity())) {
            UIHelp.PlayMusic(getActivity(), contentListBean);
        } else {
            dialog.booleWifiDialog(contentListBean);
        }

    }

    private class SlideIndexGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //向左滑动
            if (e1.getX() - e2.getX() > 120) {
                Log.e("MotionEvent", "向左滑动");
                if (gvSearchRangkingGridview.getVisibility() == View.GONE) {
                    gvSearchRangkingGridview.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
                    gvSearchRangkingGridview.setVisibility(View.VISIBLE);
                    lvSearchResultSong.setVisibility(View.GONE);
                }
                return true;
            }
            return false;
        }
    }

    private void registerSlide() {
        detector = new GestureDetector(getActivity(), new SlideIndexGestureListener());
        myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public boolean onTouch(MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        };
        ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
    }

}
