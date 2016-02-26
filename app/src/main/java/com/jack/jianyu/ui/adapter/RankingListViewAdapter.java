package com.jack.jianyu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.bean.MusicContentListBean;

import java.util.List;

/**
 * author:S.jack
 * data:2016-01-13 11:15
 */


public class RankingListViewAdapter extends MultiViewTypeBaseAdapter<MusicContentListBean> {
    private Context mContext;
    private List<MusicContentListBean> mRankingLists;

    public RankingListViewAdapter(Context mContext, List mDataList) {
        super(mContext, mDataList);
        this.mContext = mContext;
        this.mRankingLists = mDataList;
    }

    @Override
    public int getItemResourceId(int type) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_rankinglist_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return getItemView(position, convertView, viewHolder, 0);
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder, int type) {
        final MusicContentListBean mRankingList = mDataList.get(position);

        TextView mNums = holder.getView(R.id.search_rankinglist_nums_tv);
        TextView mSongName = holder.getView(R.id.search_rankinglist_item_song);
        TextView mArtistName = holder.getView(R.id.search_rankinglist_item_artist);

        if (position == 0) {
            mNums.setText(String.valueOf(position + 1));
            mNums.setTextColor(mContext.getResources().getColor(R.color.orangeDark));
        } else if (position == 1) {
            mNums.setText(String.valueOf(position + 1));
            mNums.setTextColor(mContext.getResources().getColor(R.color.orangeMedium));
        } else if (position == 2) {
            mNums.setText(String.valueOf(position + 1));
            mNums.setTextColor(mContext.getResources().getColor(R.color.orangePrimary));
        } else {
            mNums.setText(String.valueOf(position + 1));
            mNums.setTextColor(mContext.getResources().getColor(R.color.darkgrey));
        }
        mSongName.setText(mRankingList.getSongname());
        mArtistName.setText(mRankingList.getSingername());

        return convertView;
    }
}
