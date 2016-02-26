package com.jack.jianyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 重写的ListView，使其在Scrolling布局里面能够显示所有item
 * author:S.jack
 * data:2016-01-15 15:14
 */


public class DisabledScrollListView extends ListView {
    public DisabledScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisabledScrollListView(Context context) {
        super(context);
    }

    public DisabledScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
