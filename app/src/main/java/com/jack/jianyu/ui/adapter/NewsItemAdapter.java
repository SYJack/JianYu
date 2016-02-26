package com.jack.jianyu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.bean.StoriesEntity;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * author:S.jack
 * data:2016-01-20 17:02
 */


public class NewsItemAdapter extends MultiViewTypeBaseAdapter<StoriesEntity> {
    //带图item
    private static final int NEWS_ITEM = 0;
    //日期TAG
    private static final int NEWS_DATE = 1;

    private Context mContext = null;
    private List<StoriesEntity> mListBeans = null;
    private ImageLoaderHelper imageLoaderHelper = null;
    private ImageLoader imageLoader = null;


    public NewsItemAdapter(Context mContext, List<StoriesEntity> mDataList) {
        super(mContext, mDataList);
        this.mContext = mContext;
        this.mListBeans = mDataList;
        imageLoader = ImageLoader.getInstance();
        imageLoaderHelper = ImageLoaderHelper.getInstance(mContext);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        StoriesEntity storiesEntity = mDataList.get(position);
        if (storiesEntity.getIsTag()) {
            return NEWS_ITEM;
        } else {
            return NEWS_DATE;
        }
    }

    @Override
    public int getItemResourceId(int type) {
        switch (type) {
            case NEWS_ITEM:
                return R.layout.new_list_item;
            case NEWS_DATE:
                return R.layout.list_date_item;
        }
        return 0;
    }

    @Override
    public void updateData(List<StoriesEntity> list) {
        this.mListBeans.addAll(list);
        notifyDataSetChanged();
    }

//    public void addList(List<StoriesEntity> items) {
//        this.mListBeans.addAll(items);
//        notifyDataSetChanged();
//    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder, int type) {

        StoriesEntity storiesEntity = mDataList.get(position);

        switch (type) {
            case NEWS_ITEM: {

                ImageView newsImageView = holder.getView(R.id.iv_list_image);
                TextView newsTextView = holder.getView(R.id.list_item_title);

                newsTextView.setText(storiesEntity.getTitle());
                if (storiesEntity.getImages() != null) {
                    imageLoader.displayImage(storiesEntity.getImages().get(0), newsImageView, imageLoaderHelper.getDisplayOptions());
                }
                break;
            }
            case NEWS_DATE: {
                TextView dateView = holder.getView(R.id.date_text);
                dateView.setText(storiesEntity.getDate());
                break;
            }
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        ViewHolder viewHolder0 = null;
        ViewHolder viewHolder1 = null;

        switch (type) {
            case NEWS_ITEM: {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(getItemResourceId(type), parent, false);
                    viewHolder0 = new ViewHolder(convertView);
                    convertView.setTag(viewHolder0);
                } else {
                    viewHolder0 = (ViewHolder) convertView.getTag();
                }
                return getItemView(position, convertView, viewHolder0, type);
            }
            case NEWS_DATE: {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(getItemResourceId(type), parent, false);
                    viewHolder1 = new ViewHolder(convertView);
                    convertView.setTag(viewHolder1);
                } else {
                    viewHolder1 = (ViewHolder) convertView.getTag();
                }

                return getItemView(position, convertView, viewHolder1, type);
            }
        }
        return null;
    }

}
