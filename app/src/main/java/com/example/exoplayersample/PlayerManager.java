package com.example.exoplayersample;

import android.net.Uri;
import android.view.Surface;

/**
 * Created by 龙泉 on 2016/12/26.
 */
public interface PlayerManager {

    void init();

    void setSurface(Surface surface);

    void play();

    void setPlayUri(Uri uri);

    void release();
}
