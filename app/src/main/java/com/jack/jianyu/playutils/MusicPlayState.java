/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jack.jianyu.playutils;

public class MusicPlayState {

    public static final int MPS_PREPARED = 1; // 准备就绪

    public static final int MPS_PLAYING = 2; // 播放中

    public static final int MPS_PAUSE = 3; // 暂停

    public static final int MPS_CONTINUE = 4; // 恢复播放

    public static final int UPDATE_PROGRESS = 5;//进度跟新

    //服务要发送的一些Action
    public static final String MUSIC_CURRENT = "com.jack.action.MUSIC_CURRENT";    //当前音乐播放时间更新动作
    public static final String MUSIC_DURATION = "com.jack.action.MUSIC_DURATION";//新音乐长度更新动作
    public static final String MUSIC_INFO = "com.jack.action.MUSIC_INFO";

    public static final String ACTION_MUSIC_PLAY = "ACTION_MUSIC_PLAY"; // 开始播放
    public static final String ACTION_MUSIC_PAUSE = "ACTION_MUSIC_PAUSE"; // 暂停播放
    public static final String ACTION_MUSIC_STOP = "ACTION_MUSIC_STOP"; // 停止播放
    public static final String ACTION_MUSIC_NEXT = "ACTION_MUSIC_NEXT"; // 播放下一曲
    public static final String ACTION_MUSIC_PREV = "ACTION_MUSIC_PREV"; // 播放上一曲
    public static final String ACTION_SEEK_TO = "ACTION_SEEK_TO"; // 调节进度

    public static final String PLAYER_STATE = "player_state";// 用于传参数

}