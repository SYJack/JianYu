package com.jack.jianyu.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jack.jianyu.R;
import com.jack.jianyu.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * author:S.jack
 * data:2016-01-17 22:43
 */

public class PlayerDiscView extends RelativeLayout {
    //唱针动画时间
    private static final int NEEDLE_ANIMATOR_TIME = 350;

    //唱针转动的角度
    private static final float NEEDLE_ROTATE_CIRCLE = -30.0f;
    //旋转一周所用的时间
    private static final int DISC_ANIMATOR_TIME = 20 * 1000;
    //动画旋转重复执行的次数，这里代表无数次，似乎没有无限执行的属性，所以用了负数
    private static final int DISC_ANIMATOR_REPEAT_COUNT = -1;

    private static final int DISC_REVERSE_ANIMATOR_TIME = 500;

    private ImageView mNeedle;//唱针
    private ImageView mAlbumCover;//歌手封面
    private RelativeLayout mDiscLayout;//唱片布局

    private ObjectAnimator mNeedleAnimator;//唱针动画
    private ObjectAnimator mDiscLayoutAnimator;//唱片动画

    private float mDiscLayoutAnimatorValue;//唱片布局动画值

    //唱针初始坐标
    private float mNeedlePivotX = 0.0f;
    private float mNeedlePivotY = 0.0f;

    private static final float X_FRACTION = 184.0f / 212.0f;
    private static final float Y_FRACTION = 25.0f / 259.0f;

    private boolean isPlaying = false;

    private Context mContext;

    public boolean isPlaying() {
        return isPlaying;
    }

    public PlayerDiscView(Context context) {
        super(context);
        init(context);
    }


    public PlayerDiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PlayerDiscView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        mContext = context;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.stick);
        mNeedlePivotX = bitmap.getWidth() * X_FRACTION;
        mNeedlePivotY = bitmap.getHeight() * Y_FRACTION;

        bitmap.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNeedle = (ImageView) findViewById(R.id.player_needle);
        mAlbumCover = (ImageView) findViewById(R.id.player_disc_image);
        mDiscLayout = (RelativeLayout) findViewById(R.id.player_disc_container);


        mNeedle.setPivotX(mNeedlePivotX);//设置水平方向偏移量
        mNeedle.setPivotY(mNeedlePivotY);//设置垂直方向偏移量
    }

    public void startPlay() {
        if (isPlaying) {
            return;
        }

        startNeedleAnimator();
        startDiscAnimator(0.0f);

        isPlaying = true;
    }

    public void rePlay() {
        if (isPlaying) {
            return;
        }

        startNeedleAnimator();
        startDiscAnimator(mDiscLayoutAnimatorValue);

        isPlaying = true;
    }

    public void pause() {
        if (!isPlaying) {
            return;
        }

        startNeedleAnimator();

        if (mDiscLayoutAnimator.isRunning() || mDiscLayoutAnimator.isStarted()) {
            mDiscLayoutAnimator.cancel();
        }

        isPlaying = false;
    }

    public void next() {
        if (isPlaying) {
            startNeedleAnimator();
        }
        mDiscLayoutAnimator.cancel();
        isPlaying = false;

        reverseDiscAnimator();
    }

    private void startNeedleAnimator() {
        if (isPlaying) {
            mNeedleAnimator = ObjectAnimator.ofFloat(mNeedle, "rotation", 0, NEEDLE_ROTATE_CIRCLE);
        } else {
            mNeedleAnimator = ObjectAnimator.ofFloat(mNeedle, "rotation", NEEDLE_ROTATE_CIRCLE, 0);
        }

        mNeedleAnimator.setDuration(NEEDLE_ANIMATOR_TIME);
        mNeedleAnimator.setInterpolator(new DecelerateInterpolator());

        if (mNeedleAnimator.isRunning() || mNeedleAnimator.isStarted()) {
            mNeedleAnimator.cancel();
        }

        mNeedleAnimator.start();
    }

    private void startDiscAnimator(float animatedValue) {
        mDiscLayoutAnimator = ObjectAnimator.ofFloat(mDiscLayout, "rotation", animatedValue, 360 + animatedValue);
        mDiscLayoutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                mDiscLayoutAnimatorValue = (Float) arg0.getAnimatedValue();
            }
        });
        mDiscLayoutAnimator.setDuration(DISC_ANIMATOR_TIME);
        mDiscLayoutAnimator.setRepeatCount(DISC_ANIMATOR_REPEAT_COUNT);
        mDiscLayoutAnimator.setInterpolator(new LinearInterpolator());

        if (mDiscLayoutAnimator.isRunning() || mDiscLayoutAnimator.isStarted()) {
            mDiscLayoutAnimator.cancel();
        }

        mDiscLayoutAnimator.start();
    }

    private void reverseDiscAnimator() {
        mDiscLayoutAnimator = ObjectAnimator.ofFloat(mDiscLayout, "rotation", mDiscLayoutAnimatorValue, 360);
        mDiscLayoutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                mDiscLayoutAnimatorValue = (Float) arg0.getAnimatedValue();
            }
        });
        mDiscLayoutAnimator.setDuration(DISC_REVERSE_ANIMATOR_TIME);
        mDiscLayoutAnimator.setInterpolator(new AccelerateInterpolator());

        if (mDiscLayoutAnimator.isRunning() || mDiscLayoutAnimator.isStarted()) {
            mDiscLayoutAnimator.cancel();
        }

        mDiscLayoutAnimator.start();
    }

    public void loadAlbumCover(String imageUrl) {
        ImageLoader.getInstance().displayImage(imageUrl, mAlbumCover,
                ImageLoaderHelper.getInstance
                        (mContext).getDisplayOptions());
    }
}
