package com.jack.jianyu.utils.animation;/**
 * author:S.jack
 * data:2016-01-14 18:26
 */

import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


public class LoadingAnimation {
    public static void showLoadingAnimation(View view,ImageView imageView) {
        // 显示布局
        view.setVisibility(View.VISIBLE);
        // 开始旋转动画
        RotateAnimation anim = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(-1);
        imageView.startAnimation(anim);
    }

    // 关闭loading的动画
    public static void closeLoadingAnimation(View view) {
        view.setVisibility(View.GONE);
    }
}
