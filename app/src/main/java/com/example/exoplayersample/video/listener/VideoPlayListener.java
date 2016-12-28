package com.example.exoplayersample.video.listener;

/**
 * Created by 龙泉 on 2016/10/27.
 */

public interface VideoPlayListener {
    void playFinished(boolean playWhenReady);

    void playStarted();

    void playerReady(boolean playWhenReady);

    void onError(Exception ex);
}
