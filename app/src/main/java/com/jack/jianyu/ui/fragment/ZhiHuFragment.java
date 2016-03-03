package com.jack.jianyu.ui.fragment;/**
 * author:S.jack
 * data:2016-01-13 10:50
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseFragment;
import com.jack.jianyu.bean.BeforeBean;
import com.jack.jianyu.bean.StoriesEntity;
import com.jack.jianyu.bean.ZhiHuNewsBean;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.ui.activity.NewContentActivity;
import com.jack.jianyu.ui.adapter.NewsImagePaperAdapter;
import com.jack.jianyu.ui.adapter.NewsItemAdapter;
import com.jack.jianyu.utils.GsonUtil;
import com.jack.jianyu.utils.NetWorkUtils;
import com.jack.jianyu.utils.animation.LoadingAnimation;
import com.jack.jianyu.utils.http.HttpUtils;
import com.jack.jianyu.widget.AutoScrollViewPager;
import com.jack.jianyu.widget.SimpleSwipeRefreshLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;


public class ZhiHuFragment extends BaseFragment implements View.OnTouchListener, AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.news_list)
    ListView newsList;

    private NewsItemAdapter mItemAdapter;

    @InjectView(R.id.search_loading_img)
    ImageView searchLoadingImg;

    @InjectView(R.id.search_loading_layout)
    LinearLayout searchLoadingLayout;

    @InjectView(R.id.ll_error_layout)
    LinearLayout llErrorLayout;


    @InjectView(R.id.swipe_refresh_layout)
    SimpleSwipeRefreshLayout swipeRefreshLayout;

    private List<ZhiHuNewsBean.TopStoriesEntity> topStories;
    private List<StoriesEntity> storiesEntities = new ArrayList<>();

    private AutoScrollViewPager newsViewpage;

    private CircleIndicator indicator;

    private View headerView;

    private String date;

    private boolean isLoading = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LoadingAnimation.closeLoadingAnimation(searchLoadingLayout);
                    llErrorLayout.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    LoadingAnimation.closeLoadingAnimation(searchLoadingLayout);
                    initViewPage();
                    initNewsList();
                    break;
            }
        }
    };

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zhihu, container, false);

        ButterKnife.inject(this, view);

        setHeaderView();
        getNewsData();

        swipeRefreshLayout.setViewGroup(newsList);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);

        newsList.setOnItemClickListener(this);
        newsList.setOnScrollListener(this);
        return view;
    }

    private void getNewsData() {
        if (NetWorkUtils.isNetworkConnected(getActivity())) {
            LoadingAnimation.showLoadingAnimation(searchLoadingLayout, searchLoadingImg);
            HttpUtils.get(AppConstant.ZhiHuUrl.URL_LATEST, responseHandler);
        } else {
            handler.sendMessage(handler.obtainMessage(0));
        }
    }

    private void setHeaderView() {
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.news_header, null);
        newsViewpage = (AutoScrollViewPager) headerView.findViewById(R.id.news_viewpage);
        indicator = (CircleIndicator) headerView.findViewById(R.id.indicator_default);
    }

    private void initViewPage() {
        newsViewpage.setOnTouchListener(this);
        newsList.addHeaderView(headerView);
        newsViewpage.setAdapter(new NewsImagePaperAdapter(getActivity(), topStories).setInfiniteLoop(false));
        newsViewpage.setInterval(4000);
        newsViewpage.startAutoScroll();
        newsViewpage.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % topStories.size());
        indicator.setViewPager(newsViewpage);
    }

    private void initNewsList() {
        if (storiesEntities != null) {
            if (mItemAdapter == null) {
                mItemAdapter = new NewsItemAdapter(getActivity(), storiesEntities);
                newsList.setAdapter(mItemAdapter);
            }
        }
    }

    private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                ZhiHuNewsBean newsBean = GsonUtil.getData(new String(bytes, "utf-8"), ZhiHuNewsBean.class);
                topStories = newsBean.getTop_stories();

                storiesEntities = newsBean.getStories();

                date = newsBean.getDate();
                handler.sendMessage(handler.obtainMessage(1));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    };

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        newsViewpage.stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        // start auto scroll when onResume
        newsViewpage.startAutoScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 滑动ViewPager引起swiperefreshlayout刷新的冲突解决
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                swipeRefreshLayout.setEnabled(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                swipeRefreshLayout.setEnabled(true);
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StoriesEntity storiesEntity = (StoriesEntity) parent.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), NewContentActivity.class);
        intent.putExtra("zhihuBean", storiesEntity);
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (newsList != null && newsList.getChildCount() > 0) {
            if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                loadMore(AppConstant.ZhiHuUrl.URLDEFORE + date);
            }
        }
    }

    private void loadMore(String url) {
        isLoading = true;
        if (NetWorkUtils.isNetworkConnected(getActivity())) {
            HttpUtils.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        parseBeforeJson(new String(bytes, "utf-8"));
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

    private void parseBeforeJson(String responseString) {
        Log.e("responseString", responseString);
        BeforeBean beforeBean = GsonUtil.getData(responseString, BeforeBean.class);
        if (beforeBean == null) {
            isLoading = false;
            return;
        }
        date = beforeBean.getDate();
        List<StoriesEntity> moreStoriesItem = beforeBean.getStories();
//        storiesEntities.addAll(moreStoriesItem);
        mItemAdapter.updateData(moreStoriesItem);
        isLoading = false;
    }
}
