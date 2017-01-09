package com.younchen.myexoplayer.player.listener;

/**
 * Created by 龙泉 on 2017/1/6.
 * ui used to receive event from owner to update ui.
 */
public interface PlayerCallBack {

    void onBuffering();

    void onStartPlay();

    void onError();

    void onPausePlay();

    void onEndPlay();

    void onTimeChanged(String playTime, String totalTime);

    void onBufferChanged(int currentBuffer);

    void onProgressChanged(int progress);

}
