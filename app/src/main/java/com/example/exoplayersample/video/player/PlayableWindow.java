package com.example.exoplayersample.video.player;

import android.view.Surface;
import android.view.View;

import com.example.exoplayersample.video.player.manager.VideoPlayManager;


/**
 * Created by 龙泉 on 2016/10/19.
 */

public interface PlayableWindow<T> {

    boolean canPlay();

    View getPlayerView();

    View getVideoView();


    Surface getPlayableSurface();

    void updateUiToPlayState();

    void updateUiToPauseState();

    void updateUiToNormalState();

    void updateUiToFocusState();

    void updateUiToPrepare();

    void showLoading();

    void hideLoading();

    void showCover();

    void hideCover();

    float getWindowLastCalculateArea();

    void setCurrentSeek(long playTime);

    long getCurrentSeek();

    void setWindowIndex(int index);

    int getWindowIndex();

    void onRelease();

    //----new --------
    void stopPlay();

    boolean isPlaying();

    void setUrl(String url);

    void play();

    void pause();

    void resume();

    void onFocus();

    void setSurface(Surface mSurface);

    void setVideoPlayManager(VideoPlayManager videoPlayManager);

    T getVideoItem();

    void updateVideoItem(T videoItem);

    boolean playActive();

    void setPlayActive(boolean playActive);
}
