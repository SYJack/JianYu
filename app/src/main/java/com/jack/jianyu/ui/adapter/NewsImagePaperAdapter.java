package com.jack.jianyu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.bean.StoriesEntity;
import com.jack.jianyu.bean.ZhiHuNewsBean;
import com.jack.jianyu.ui.activity.NewContentActivity;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import jakewharton.salvage.RecyclingPagerAdapter;

/**
 * author:S.jack
 * data:2016-01-21 00:25
 */


public class NewsImagePaperAdapter extends RecyclingPagerAdapter {

    private Context mContext = null;
    private List<ZhiHuNewsBean.TopStoriesEntity> mHeaderImageBeans = null;

    private boolean isInfiniteLoop;
    private ImageLoaderHelper imageLoaderHelper = null;
    private ImageLoader imageLoader = null;

    public NewsImagePaperAdapter(Context mContext, List<ZhiHuNewsBean.TopStoriesEntity> mDataList) {
        this.mContext = mContext;
        this.mHeaderImageBeans = mDataList;
        isInfiniteLoop = false;

        imageLoader = ImageLoader.getInstance();
        imageLoaderHelper = ImageLoaderHelper.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : mHeaderImageBeans.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % mHeaderImageBeans.size() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        final ZhiHuNewsBean.TopStoriesEntity imageBean = mHeaderImageBeans.get(position);

        final StoriesEntity storiesEntity = new StoriesEntity();
        storiesEntity.setId(imageBean.getId());
        storiesEntity.setTitle(imageBean.getTitle());

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_header_title, container, false);
            viewHolder.mHeaderImage = (ImageView) convertView.findViewById(R.id.iv_news_image);
            viewHolder.mHeaderTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(imageBean.getImage(), viewHolder.mHeaderImage, imageLoaderHelper.getDisplayOptions());
        viewHolder.mHeaderTitle.setText(imageBean.getTitle());

        viewHolder.mHeaderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewContentActivity.class);
                intent.putExtra("zhihuBean", storiesEntity);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView mHeaderImage;
        TextView mHeaderTitle;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public NewsImagePaperAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
