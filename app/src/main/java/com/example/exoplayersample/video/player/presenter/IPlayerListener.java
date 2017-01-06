package com.example.exoplayersample.video.player.presenter;

/**
 * Created by 龙泉 on 2017/1/6.
 */
public interface IPlayerListener {

    void onBuffering();

    void onStartPlay();

    void onError();

    void onPausePlay();

    void onEndPlay();

    void onTimeChanged(String playTime, String totalTime);

    void onBufferChanged(int currentBuffer);

    void onProgressChanged(int progress);

}
