package com.jack.jianyu.ui;

import android.app.Activity;
import android.content.Context;

import com.jack.jianyu.R;
import com.jack.jianyu.constant.AppConstant;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by jack on 2016/2/18.
 */
public class ShareMySoftWare {

    private Context mContext;

    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(AppConstant.DESCRIPTOR);

    public ShareMySoftWare(Context mContext) {
        this.mContext = mContext;
        init();
        configPlatforms();
        setShareContent();
    }

    private void init() {
        mController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT);
        mController.openShare((Activity) mContext, false);
    }

    private void configPlatforms() {

        addWXPlatform();
        addQQQZonePlatform();
    }

    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wxa8213dc827399101";
        String appSecret = "5c716417ce72ff69d8cf0c43572c9284";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void addQQQZonePlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) mContext, appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) mContext, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }


    /**
     * 设置分享内容
     */
    private void setShareContent() {
        UMImage localImage = new UMImage(mContext, R.mipmap.ic_launcher);
        //设置微信好友分享内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        //设置分享文字
        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
        //设置title
        weixinContent.setTitle("友盟社会化分享组件-微信");
        //设置分享内容跳转URL
        weixinContent.setTargetUrl("你的URL链接");
        //设置分享图片
        weixinContent.setShareImage(localImage);
        mController.setShareMedia(weixinContent);

        //设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
//设置朋友圈title
        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
        circleMedia.setShareImage(localImage);
        circleMedia.setTargetUrl("你的URL链接");
        mController.setShareMedia(circleMedia);


        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent("亲们，我现在用的软件不错，去下载玩玩~");
        qzone.setTargetUrl("http://www.wandoujia.com/apps/com.jack.jianyu");
        qzone.setTitle("来自简娱");
        qzone.setShareMedia(localImage);
        mController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("亲，我现在用的软件不错，去下载玩玩~");
        qqShareContent.setTitle("hello, 简娱");
        qqShareContent.setShareMedia(localImage);
        qqShareContent.setTargetUrl("http://www.wandoujia.com/apps/com.jack.jianyu");
        mController.setShareMedia(qqShareContent);
    }

}
