package com.younchen.myexoplayer.player.listener;

/**
 * Created by 龙泉 on 2016/12/27.
 */

public interface PlayerListener {

    void onLoading(boolean isLoading);

    void onPlayEnd();

    void onStartPlay();

    void onError(Exception error);

    void onPausePlay();

}
