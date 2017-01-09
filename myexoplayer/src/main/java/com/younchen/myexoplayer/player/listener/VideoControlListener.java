package com.younchen.myexoplayer.player.listener;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */
public interface VideoControlListener {

     void onPlayClicked() ;

    void onSeek(int progress);

    void onEnterFullScreenMode();

    void onQuitFullScreenMode();

    void onSpeedUp();

    void onSpeedDown();
}
