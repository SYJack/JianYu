package com.jack.jianyu.ui.fragment;
/**
 * author:S.jack
 * data:2016-01-13 10:55
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseFragment;
import com.jack.jianyu.bean.ChatMessageBean;
import com.jack.jianyu.bean.TuLingMsgBean;
import com.jack.jianyu.constant.AppConstant;
import com.jack.jianyu.ui.adapter.ChatMessageAdapter;
import com.jack.jianyu.utils.GsonUtil;
import com.jack.jianyu.utils.NetWorkUtils;
import com.jack.jianyu.utils.ToastHelper;
import com.jack.jianyu.utils.http.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AndroidFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    @InjectView(R.id.emoji_title_flag)
    CheckBox emojiTitleFlag;

    @InjectView(R.id.info_titile_input)
    AppCompatEditText infoTitileInput;

    @InjectView(R.id.emoji_title_send)
    ImageView emojiTitleSend;


    @InjectView(R.id.listview_msg)
    ListView listViewMsg;


    private ChatMessageAdapter messageAdapter = null;
    private List<ChatMessageBean> messageBeans = null;

    private Bitmap userImage;

    private Bitmap tuLingImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_android_chat, container, false);
        ButterKnife.inject(this, view);
        initData();
        initOnClickListener();
        initUserImageView();
        return view;
    }

    private void initUserImageView() {
        String pathName = Environment.getExternalStorageDirectory()
                + AppConstant.Paths.USER_IMAGE_PHOTO + "logo.jpg";
        File file = new File(pathName);
        if (file.exists()) {
            userImage = BitmapFactory.decodeFile(pathName);
        }
    }

    private void initOnClickListener() {
        emojiTitleFlag.setOnCheckedChangeListener(this);
        emojiTitleSend.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            hideSoftKeyboard();
        } else {
            showSoftKeyboard();
        }
    }

    @Override
    public void onClick(View v) {
        final String text = infoTitileInput.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            ChatMessageBean toMessageBean = new ChatMessageBean();
            toMessageBean.setDate(new Date());
            toMessageBean.setMsg(text);
            toMessageBean.setRoundImageView(userImage);
            toMessageBean.setMsgType(false);
            messageAdapter.addList(toMessageBean);
            listViewMsg.setSelection(messageBeans.size() - 1);

            infoTitileInput.setText("");

            if (NetWorkUtils.isNetworkConnected(getActivity())) {

                RequestParams params = new RequestParams();
                params.put("key", AppConstant.TuLingUrl.KEY_TULING);
                params.put("info", text);

                HttpUtils.post(AppConstant.TuLingUrl.URL_TULING, params, responseHandler);
            }
        } else {
            ToastHelper.showShort(getActivity(), "说点什么吧");
        }
    }


    private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            try {
                ChatMessageBean messageBean = new ChatMessageBean();
                TuLingMsgBean tuLingMsgBean = GsonUtil.getData(new String(bytes, "utf-8"), TuLingMsgBean.class);
                if (tuLingMsgBean.getUrl() != null) {
                    messageBean.setMsg(tuLingMsgBean.getTulingResult() + Html.fromHtml(tuLingMsgBean.getUrl()));
                } else {
                    messageBean.setMsg(tuLingMsgBean.getTulingResult());
                }
                messageBean.setMsgType(true);
                messageBean.setDate(new Date());

                messageAdapter.addList(messageBean);
                listViewMsg.setSelection(messageBeans.size() - 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    };

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                infoTitileInput.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyboard() {
        infoTitileInput.requestFocus();
        ((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(infoTitileInput,
                InputMethodManager.SHOW_FORCED);
    }

    private void initData() {
        tuLingImage = BitmapFactory.decodeResource(getResources(), R.mipmap.androidrobot);
        messageBeans = new ArrayList<>();
        messageBeans.add(new ChatMessageBean("你好，我是人见人爱，车见车载的图灵机器人~", new Date(), tuLingImage, true));
        messageAdapter = new ChatMessageAdapter(getActivity(), messageBeans);
        listViewMsg.setAdapter(messageAdapter);
    }
}
