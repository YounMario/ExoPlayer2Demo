package com.example.exoplayersample.video.player.presenter;

import android.view.View;
import android.view.ViewGroup;

import com.example.exoplayersample.video.player.listener.VideoControlListener;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public interface IPlayerView {

    View getVideoView();

    ViewGroup getVideoContainer();

    View getProgressBar();

    void onBuffering();

    void onStartPlay();

    void onError();

    void setControlListener(VideoControlListener videoControlListener);

    void onPausePlay();

    void onTimeChanged(String playTime, String totalTime);
}
