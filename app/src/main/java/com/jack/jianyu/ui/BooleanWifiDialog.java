package com.jack.jianyu.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.jack.jianyu.bean.MusicContentListBean;

/**
 * Created by jack on 2016/2/20.
 */
public class BooleanWifiDialog {

    private Context mContext;

    public BooleanWifiDialog(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 判断wifi网络打开
     */
    public void booleWifiDialog(final MusicContentListBean rankingSonglist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("当前不是WiFi网络，是否打开？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UIHelp.PlayMusic(mContext, rankingSonglist);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
