package com.jack.jianyu.playutils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;


/**
 * Created by jack on 2016/2/13.
 */
public class MusicPlayer {

    private static final String TAG = MusicPlayer.class.getSimpleName();

    private static MusicPlayer musicPlayer;

    private static MediaPlayer mMediaPlayer;

    public MusicPlayer() {
    }

    public synchronized static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        return musicPlayer;
    }

    /**
     * 播放在线的歌曲
     *
     * @param context 上下文
     * @param uri     歌曲uri
     */
    public void playOnline(Context context, Uri uri) {
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                MusicPlayService.notifyBufferingUpdateListener(percent);
            }
        });
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     */
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 恢复音乐播放
     **/
    public void resume() {

        mMediaPlayer.start();
    }

    /**
     * 停止
     */
    public void stop() {
        mMediaPlayer.stop();
    }

    /**
     * 释放
     */
    public void release() {
        mMediaPlayer.release();
    }

    /**
     * 获取歌曲当前播放位置
     *
     * @return int 歌曲位置
     */
    public int getPlayCurrentTime() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 获取歌曲时长
     *
     * @return int 歌曲时长
     */
    public int getPlayDuration() {
        return mMediaPlayer.getDuration();
    }

    /**
     * 按移动的指定的位置播放
     *
     * @param seek 指定的位置
     */
    public void seekToMusicAndPlay(int seek) {
        mMediaPlayer.seekTo(seek);
        mMediaPlayer.start();
    }

    /**
     * 按移动的指定的位置
     *
     * @param seek 指定的位置
     */
    public void seekToMusic(int seek) {
        mMediaPlayer.seekTo(seek);
    }

    /**
     * 判断当前是否在播放
     *
     * @return boolean
     */
    public Boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }


}
