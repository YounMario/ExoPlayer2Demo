package com.younchen.myexoplayer.player;

import android.net.Uri;
import android.view.Surface;

import com.younchen.myexoplayer.player.listener.PlayerListener;


/**
 * Created by 龙泉 on 2016/12/26.
 */
public interface Player {

    void setSurface(Surface surface);

    void play();

    Player setPlayUri(Uri uri);

    void release();

    void setPlayerListener(PlayerListener listener);

    long getDuration();

    long getCurrentPosition();

    long getBufferedPercentage();

    boolean isPlaying();

    void pause();

    void seekTo(long position);

    void resume();

    void stop();
}
