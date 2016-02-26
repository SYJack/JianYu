package com.jack.jianyu.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.bean.ChatMessageBean;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.jack.jianyu.widget.XCRoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jack on 2016/2/6.
 */
public class ChatMessageAdapter extends MultiViewTypeBaseAdapter<ChatMessageBean> {

    private Context mContext = null;
    private List<ChatMessageBean> messageBeans = null;
    private LayoutInflater mInflater = null;

    private ImageLoaderHelper imageLoaderHelper = null;
    private ImageLoader imageLoader = null;

    private Bitmap userImage;

    //ListView视图的内容由IMsgViewType决定
    public interface IMsgViewType {
        //对方发来的信息
        int IMVT_COM_MSG = 0;
        //自己发出的信息
        int IMVT_TO_MSG = 1;
    }

    public ChatMessageAdapter(Context mContext, List<ChatMessageBean> mDataList) {
        super(mContext, mDataList);
        this.mContext = mContext;
        this.messageBeans = mDataList;
        mInflater = LayoutInflater.from(mContext);

        imageLoader = ImageLoader.getInstance();
        imageLoaderHelper = ImageLoaderHelper.getInstance(mContext);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemResourceId(int type) {
        switch (type) {
            case IMsgViewType.IMVT_COM_MSG:
                return R.layout.item_chat_receivemsg;
            case IMsgViewType.IMVT_TO_MSG:
                return R.layout.item_chat_sendmsg;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageBean chatMessageBean = messageBeans.get(position);
        if (chatMessageBean.getMsgType()) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder, int type) {
        ChatMessageBean chatMessageBean = messageBeans.get(position);

        TextView tvSendTime = holder.getView(R.id.tv_sendTime);
        TextView tvContent = holder.getView(R.id.tv_content);

        XCRoundImageView imageView = holder.getView(R.id.image_tuling);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        tvSendTime.setText(sdf.format(chatMessageBean.getDate()));
        tvContent.setText(chatMessageBean.getMsg());
        imageView.setImageBitmap(chatMessageBean.getRoundImageView());
//        imageLoader.displayImage( imageView, imageLoaderHelper.getDisplayOptions());

        return convertView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        ChatMessageBean chatMessageBean = messageBeans.get(position);

        boolean isComMsg = chatMessageBean.getMsgType();

        ViewHolder viewHolder = null;

        if (convertView == null) {
            if (isComMsg) {
                //如果是对方发来的消息，则显示的是左气泡
                convertView = mInflater.inflate(getItemResourceId(type), parent, false);
            } else {
                //如果是自己发出的消息，则显示的是右气泡
                convertView = mInflater.inflate(getItemResourceId(type), parent, false);
            }
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return getItemView(position, convertView, viewHolder, type);
    }

    public void addList(ChatMessageBean message) {
        this.messageBeans.add(message);
        notifyDataSetChanged();
    }
}
