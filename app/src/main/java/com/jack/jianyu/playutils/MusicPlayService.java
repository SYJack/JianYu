package com.jack.jianyu.playutils;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.jack.jianyu.bean.MusicContentListBean;
import com.jack.jianyu.playutils.playlistener.OnBufferingUpdateListener;
import com.jack.jianyu.playutils.playlistener.OnPlayerStateChangeListener;
import com.jack.jianyu.playutils.playlistener.OnSeekChangeListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2016/2/12.
 */
public class MusicPlayService extends Service implements Runnable {

    public static final String TAG = MusicPlayService.class.getSimpleName();

    public MusicPlayerReceiver receiver;

    public static MusicPlayer mPlayer;
    //播放状态是否改变,进度条是否改变
    public static boolean stateChange, seekChange;
    //当前歌曲进度条最大值
    public static int max = 0;
    // 当前歌曲播放进度
    public static int progress = 0;
    // 当前播放的时间
    public static String timePosition = "0:00";
    // 当前歌曲播放的时长
    public static String duration = "0:00";
    //歌曲相关信息
    public static MusicContentListBean mSonginfo;

    // 常驻线程是否运行
    public static Boolean isRun = true;

    // 当前音乐播放状态，默认为等待
    public static int state = MusicPlayState.MPS_PREPARED;


    // 用一个List保存 客户注册的监听----用于回调更新客户的ui、seekbar进度改变的监听
    public static List<OnPlayerStateChangeListener> stateListenerList = new ArrayList<OnPlayerStateChangeListener>();
    private static List<OnSeekChangeListener> seekListenerList = new ArrayList<OnSeekChangeListener>();
    private static List<WeakReference<OnBufferingUpdateListener>> bufferingUpdateListenerList = new ArrayList<WeakReference<OnBufferingUpdateListener>>();

    // handler匿名内部类，用于监听器遍历回调
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 对List中的所有监听器遍历回调，根据what值判断回调哪个监听器
            switch (msg.what) {
                case 0:
                    for (OnPlayerStateChangeListener playerListener : stateListenerList)
                        playerListener.onStateChange(state, mSonginfo);
                    break;
                case 1:
                    for (OnSeekChangeListener seekChangeListener : seekListenerList)
                        seekChangeListener.onSeekChange(progress, max, timePosition, duration);
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new MusicPlayerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayerReceiver.ACTION_PLAY_ITEM);//播放列表
        filter.addAction(MusicPlayerReceiver.ACTION_PLAY_BUTTON); // 播放和暂停键
        filter.addAction(MusicPlayerReceiver.ACTION_PLAY_PREVIOUS); // 上一首
        filter.addAction(MusicPlayerReceiver.ACTION_PLAY_NEXT); // 下一首
        filter.addAction(MusicPlayerReceiver.ACTION_SEEKBAR); // 进度条
        registerReceiver(receiver, filter);

        mPlayer = MusicPlayer.getInstance();

        // 开启常驻线程
        new Thread(this).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void run() {

        // 常驻线程，死循环
        while (isRun) {
            if (stateChange) {
                switch (state) {
                    case MusicPlayState.MPS_PREPARED:
                        break;
                    case MusicPlayState.MPS_PLAYING:
                        String url;
                        if (mSonginfo.getUrl() != null) {
                            url = mSonginfo.getUrl();
                        } else {
                            url = mSonginfo.getM4a();
                        }
                        mPlayer.playOnline(this, Uri.parse(url));
                        // 播放状态要移动seekbar
                        seekChange = true;
                        break;
                    case MusicPlayState.MPS_PAUSE:
                        mPlayer.pause();
                        break;
                    case MusicPlayState.MPS_CONTINUE:
                        mPlayer.resume();
                        // 播放状态要动seekbar
                        seekChange = true;
                        break;
                    default:
                        break;
                }
                // state改变为false
                stateChange = false;
                // 向handler发送一条消息，通知handler执行回调函数
                handler.sendEmptyMessage(0);
            }

            if (mPlayer.isPlaying()) {
                seekChange = true;
            } else {
                seekChange = false;
            }

            //如果进度条发生了改变，执行以下
            if (seekChange) {
                // 得到当前播放时间，int，毫秒单位，也是进度条的当前进度
                progress = mPlayer.getPlayCurrentTime();
                // 得到歌曲播放总时长，为进度条的最大值
                max = mPlayer.getPlayDuration();
                // 当前播放时间改变单位为分
                float floatTime = (float) progress / 1000.0f / 60.0f;

                // 当前播放时间转换为字符类型
                String timeStr = Float.toString(floatTime);
                // 根据小数点切分
                String timeSub[] = timeStr.split("\\.");
                // 初始值为0.0，在后边补0
                if (timeSub[1].length() < 2) {
                    timeSub[1] = timeSub[1] + "0";
                } else {
                    // 截取小数点后两位
                    timeSub[1] = timeSub[1].substring(0, 2);
                }

                float sec = Float.parseFloat(timeSub[1]) * 0.6f;
                String secStr = "";
                if (sec < 10.0) {
                    String secSub[] = String.valueOf(sec).split("\\.");
                    secStr = "0" + secSub[0];
                } else {
                    String secSub[] = String.valueOf(sec).split("\\.");
                    secStr = secSub[0];
                }

                // 拼接得到当前播放时间，用于UI界面显示
                timePosition = timeSub[0] + ":" + secStr;
                // seekChange改回false
                seekChange = false;
                try {
                    // 等1s发送消息,即每秒刷新时间进度
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 发送相应消息给handler
                handler.sendEmptyMessage(1);
            }
        }
    }

    public static void playerSeekToTime(int porgress, int state) {
        if (state == MusicPlayState.MPS_PLAYING || state == MusicPlayState.MPS_CONTINUE)
            mPlayer.seekToMusicAndPlay(porgress);
        else
            mPlayer.seekToMusic(porgress);
    }

    /**
     * 向service注册一个监听器，用于监听播放状态的改变
     *
     * @param listener 监听器对象
     */
    public static void registerStateChangeListener(
            OnPlayerStateChangeListener listener) {
        listener.onStateChange(state, mSonginfo);
        stateListenerList.add(listener);
    }

    /**
     * 解除之前注册的播放状态改变监听器
     *
     * @param listener 监听器对象
     */
    public static void removeStateChangeListener(
            OnPlayerStateChangeListener listener) {
        stateListenerList.remove(listener);
    }

    /**
     * 向service注册一个监听器，用于进度条的改变
     *
     * @param seekListener 监听器对象
     */
    public static void registerSeekChangeListener(OnSeekChangeListener seekListener) {
        seekListener.onSeekChange(progress, max, timePosition, duration);
        seekListenerList.add(seekListener);
    }

    /**
     * 解除之前注册的进度条监听器
     *
     * @param seekListener 监听器对象
     */
    public static void removeSeekChangeListener(OnSeekChangeListener seekListener) {
        seekListenerList.remove(seekListener);
    }

    public static void addBufferingUpdateListener(OnBufferingUpdateListener listener) {
        synchronized (listener) {
            boolean isExist = false;
            for (WeakReference<OnBufferingUpdateListener> refListener : bufferingUpdateListenerList) {
                if (refListener.get() != null && refListener.get() == listener) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                bufferingUpdateListenerList.add(new WeakReference<OnBufferingUpdateListener>(listener));
            }
        }
    }

    public static void removeBufferingUpdateListener(OnBufferingUpdateListener listener) {
        synchronized (listener) {
            for (WeakReference<OnBufferingUpdateListener> refListener : bufferingUpdateListenerList) {
                if (refListener.get() != null && refListener.get() == listener) {
                    bufferingUpdateListenerList.remove(refListener);
                    break;
                }
            }
        }
    }

    public static void notifyBufferingUpdateListener(int percent) {
        synchronized (bufferingUpdateListenerList) {
            List<WeakReference<OnBufferingUpdateListener>> emptyList = new ArrayList<WeakReference<OnBufferingUpdateListener>>();

            for (WeakReference<OnBufferingUpdateListener> refListener : bufferingUpdateListenerList) {
                OnBufferingUpdateListener listener = refListener.get();
                if (listener != null) {
                    listener.onBufferingUpdate(percent);
                } else {
                    emptyList.add(refListener);
                }
            }

            if (emptyList.size() > 0) {
                bufferingUpdateListenerList.remove(emptyList);
            }
        }
    }
}