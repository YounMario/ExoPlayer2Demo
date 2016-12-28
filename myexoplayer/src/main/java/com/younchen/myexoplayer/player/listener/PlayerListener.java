package com.younchen.myexoplayer.player.listener;

import com.google.android.exoplayer2.ExoPlaybackException;

/**
 * Created by 龙泉 on 2016/12/27.
 */

public interface PlayerListener {

    void onBuffering();

    void onPlayEnd();

    void onStartPlay();

    void onError(Exception error);
}
