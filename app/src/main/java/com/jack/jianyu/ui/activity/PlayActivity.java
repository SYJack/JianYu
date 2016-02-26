package com.jack.jianyu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jack.jianyu.R;
import com.jack.jianyu.base.BaseActivity;
import com.jack.jianyu.bean.MusicContentListBean;
import com.jack.jianyu.playutils.MusicPlayService;
import com.jack.jianyu.playutils.MusicPlayState;
import com.jack.jianyu.playutils.MusicPlayerReceiver;
import com.jack.jianyu.playutils.playlistener.OnBufferingUpdateListener;
import com.jack.jianyu.playutils.playlistener.OnPlayerStateChangeListener;
import com.jack.jianyu.playutils.playlistener.OnSeekChangeListener;
import com.jack.jianyu.utils.CommonUtils;
import com.jack.jianyu.utils.NetWorkUtils;
import com.jack.jianyu.utils.ToastHelper;
import com.jack.jianyu.widget.MultiStateView;
import com.jack.jianyu.widget.PlayerDiscView;
import com.jack.jianyu.widget.XCRoundImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jack on 2016/2/21.
 */
public class PlayActivity extends BaseActivity implements
        SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    @InjectView(R.id.player_disc_image)
    XCRoundImageView playerDiscImage;

    @InjectView(R.id.music_play_disc_view)
    PlayerDiscView mPlayerDiscView;

    @InjectView(R.id.player_needle)
    ImageView playerNeedle;

    @InjectView(R.id.musics_song_name)
    TextView musicsSongName;

    @InjectView(R.id.musics_player_songer_name)
    TextView musicsPlayerSongerName;

    @InjectView(R.id.musics_player_current_time)
    TextView musicsPlayerCurrentTime;

    @InjectView(R.id.musics_player_total_time)
    TextView musicsPlayerTotalTime;

    @InjectView(R.id.musics_player_play_prev_btn)
    ImageButton musicsPlayerPlayPrevBtn;

    @InjectView(R.id.musics_player_play_ctrl_btn)
    MultiStateView musicsPlayerPlayCtrlBtn;

    @InjectView(R.id.musics_player_play_next_btn)
    ImageButton musicsPlayerPlayNextBtn;

    @InjectView(R.id.seek_bar)
    SeekBar timeSbProgress;


    // 当前歌曲的播放状态
    private int playerState;

    // 回调接口更新UI---播放状态、进度条
    private OnPlayerStateChangeListener stateChangeListener;
    private OnSeekChangeListener seekChangeListener;
    private OnBufferingUpdateListener bufferingUpdateListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_play_music);
        ButterKnife.inject(this);
        init();
    }

    private void init() {

        musicsPlayerPlayCtrlBtn.setOnClickListener(this);

        if (NetWorkUtils.isNetworkConnected(this)) {
            initSurface();
            initData();
        } else {
            ToastHelper.showShort(this, R.string.str_error_networt);
        }
    }

    private void initData() {
        stateChangeListener = new OnPlayerStateChangeListener() {
            @Override
            public void onStateChange(int state, MusicContentListBean musicContentListBean) {
                playerState = state;
                if (musicContentListBean != null) {
                    musicsSongName.setText(musicContentListBean.getSongname());
                    musicsPlayerSongerName.setText(musicContentListBean.getSingername());
                    mPlayerDiscView.loadAlbumCover(musicContentListBean.getAlbumpic_big());
                }

                switch (state) {
                    case MusicPlayState.MPS_PLAYING:
                        musicsPlayerPlayCtrlBtn.setImageResource(R.mipmap.btn_pause_play);
                        break;
                    case MusicPlayState.MPS_CONTINUE:
                        musicsPlayerPlayCtrlBtn.setImageResource(R.mipmap.btn_pause_play);
                        break;
                    case MusicPlayState.MPS_PAUSE:
                        musicsPlayerPlayCtrlBtn.setImageResource(R.mipmap.btn_play_noral);
                        break;
                    default:
                        break;
                }
            }
        };

        seekChangeListener = new OnSeekChangeListener() {

            @Override
            public void onSeekChange(int progress, int max, String time,
                                     String duration) {
                timeSbProgress.setMax(max);
                timeSbProgress.setProgress(progress);
                musicsPlayerCurrentTime.setText(time);
                musicsPlayerTotalTime.setText(CommonUtils.durationToString(max));
            }
        };
        bufferingUpdateListener = new OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(int percent) {

                timeSbProgress.setSecondaryProgress((percent * timeSbProgress.getMax()) / 100);
            }
        };

        MusicPlayService.addBufferingUpdateListener(bufferingUpdateListener);
    }

    private void initSurface() {
        timeSbProgress.setOnSeekBarChangeListener(PlayActivity.this);
        mPlayerDiscView.startPlay();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
// 进度条改变，发送广播，回调改变播放时间
        Intent intent = new Intent(MusicPlayerReceiver.ACTION_SEEKBAR);
        intent.putExtra(MusicPlayState.ACTION_SEEK_TO,
                seekBar.getProgress());
        intent.putExtra(MusicPlayState.PLAYER_STATE, playerState);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册播放状态改变、播放模式、进度条的监听器
        MusicPlayService.registerStateChangeListener(stateChangeListener);
        MusicPlayService.registerSeekChangeListener(seekChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 解除注册状态改变、播放模式、进度条的监听器
        MusicPlayService.removeStateChangeListener(stateChangeListener);
        MusicPlayService.removeSeekChangeListener(seekChangeListener);

    }

    @Override
    protected void onDestroy() {
        if (bufferingUpdateListener != null) {
            MusicPlayService.removeBufferingUpdateListener(bufferingUpdateListener);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.musics_player_play_ctrl_btn:
                Intent intentPlay = new Intent();
                intentPlay.setAction(MusicPlayerReceiver.ACTION_PLAY_BUTTON);
                sendBroadcast(intentPlay);
                break;
        }
    }
}
