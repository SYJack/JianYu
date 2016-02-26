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
 * data:2016-01-15 14:46
 */


public class MusicSongListAdapter extends MultiViewTypeBaseAdapter<MusicContentListBean> {

    private Context mContext;
    private List<MusicContentListBean> mContentlists;

    public MusicSongListAdapter(Context mContext, List<MusicContentListBean> mDataList) {
        super(mContext, mDataList);
        this.mContext = mContext;
        this.mContentlists = mDataList;
    }

    @Override
    public int getItemResourceId(int type) {
        return 0;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder, int type) {
        final MusicContentListBean mContentlist = mDataList.get(position);

        TextView mSongName = holder.getView(R.id.search_musiclist_item_song);
        TextView mArtist = holder.getView(R.id.search_musiclist_item_artist);

        mSongName.setText(mContentlist.getSongname());
        mArtist.setText(mContentlist.getAlbumname());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_musicsonglist_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return getItemView(position, convertView, viewHolder, 0);
    }

    @Override
    public void updateData(List<MusicContentListBean> musicItems) {
        this.mContentlists.addAll(musicItems);
        notifyDataSetChanged();
    }
}
