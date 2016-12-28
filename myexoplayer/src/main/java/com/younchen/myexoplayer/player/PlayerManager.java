package com.younchen.myexoplayer.player;

import android.net.Uri;
import android.view.Surface;

import com.younchen.myexoplayer.player.listener.PlayerListener;


/**
 * Created by 龙泉 on 2016/12/26.
 */
public interface PlayerManager {

    void init();

    void setSurface(Surface surface);

    void play();

    void setPlayUri(Uri uri);

    void release();

    void setPlayerListener(PlayerListener listener);
}
