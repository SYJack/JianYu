package com.jack.jianyu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 重写GridView,使其在ScrollView中能够显示所有的图片
 * author:S.jack
 * data:2016-01-12 14:03
 */


public class DisabledScrollGridView extends GridView {
    public DisabledScrollGridView(Context context) {
        super(context);
    }

    public DisabledScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisabledScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
