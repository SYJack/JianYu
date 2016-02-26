package com.jack.jianyu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.constant.AppConstant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * author:S.jack
 * data:2016-01-13 11:17
 */

public class SearchGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> list;

    public SearchGridViewAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.search_gridview_item, null);

            viewHolder.textView = (TextView) convertView.findViewById(R.id.search_gridview_item_textview);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.search_gridview_item_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int nameID = (Integer) list.get(position).get(AppConstant.RANKING_LIST_NAME);
        int resID = (Integer) list.get(position).get(AppConstant.RANKING_LIST_RESID);

        viewHolder.imageView.setBackgroundResource(resID);
        viewHolder.textView.setText(nameID);

        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
