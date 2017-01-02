package com.example.exoplayersample.video.player.presenter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

public interface IPlayerView {

    View getVideoView();

    ViewGroup getVideoContainer();

    View getProgressBar();

    void onBuffering();

    void onStartPlay();

    void onError();
}
