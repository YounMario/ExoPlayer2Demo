package com.example.exoplayersample.video.listdemo.manager;

import android.util.Log;

import com.example.exoplayersample.video.listdemo.bean.VideoInfo;
import com.example.exoplayersample.video.listdemo.PlayableWindow;

/**
 * Created by 龙泉 on 2016/12/23.
 */

public class DefaultVideoPlayManager implements VideoPlayManager {


    private static final String TAG = "DefaultVideoPlayManager";

    private static final int STATE_NORMAL = 1;
    private static final int STATE_PLAY = 2;
    private static final int STATE_PAUSE = 3;


    private int mCurrentState;
    private PlayableWindow mCurrentWindow;

    public DefaultVideoPlayManager() {
        super();
        mCurrentState = STATE_NORMAL;
    }

    @Override
    public void play() {
        int currentPlayingPosition = mCurrentWindow.getWindowIndex();
        Log.i(TAG, "playing position:" + currentPlayingPosition);
        String url = getPlayAblePath();
        if (url == null) {
            return;
        }
        setCurrentState(STATE_PLAY);
        mCurrentWindow.setUrl(url);
        mCurrentWindow.setPlayActive(true);
        mCurrentWindow.play();
    }

    @Override
    public void stopPlay() {
        if (mCurrentWindow == null) {
            return;
        }
        Log.i(TAG, "stop play position:" + mCurrentWindow.getWindowIndex());
        setCurrentState(STATE_NORMAL);
        if (mCurrentWindow != null) {
            mCurrentWindow.stopPlay();
            mCurrentWindow.setPlayActive(false);
        }
    }

    @Override
    public void pause() {
        setCurrentState(STATE_PAUSE);
        if (mCurrentWindow != null) {
            mCurrentWindow.pause();
        }
    }

    @Override
    public PlayableWindow getCurrentPlayableWindow() {
        return mCurrentWindow;
    }

    @Override
    public void resume() {
        setCurrentState(STATE_PLAY);
        if (mCurrentWindow != null) {
            mCurrentWindow.resume();
        }
    }

    @Override
    public void onScrollFinished(boolean isUp) {

    }

    @Override
    public void release() {
        stopPlay();
        if (mCurrentWindow != null) {
            mCurrentWindow.onRelease();
            mCurrentWindow = null;
        }
    }

    @Override
    public void onAttach(PlayableWindow needPlayWindow) {
        mCurrentWindow = needPlayWindow;
        if (mCurrentState == STATE_NORMAL || mCurrentState == STATE_PAUSE) {
            play();
        }
    }

    @Override
    public void onDetach(PlayableWindow currentPlayableWindow) {
        stopPlay();
    }

    @Override
    public boolean isPlayState() {
        return mCurrentState == STATE_PLAY;
    }

    @Override
    public boolean isPauseState() {
        return mCurrentState == STATE_PAUSE;
    }


    private String getPlayAblePath() {
        if (mCurrentWindow == null) {
            return null;
        }
        final VideoInfo itemVideoInfo = (VideoInfo) mCurrentWindow.getVideoItem();
        String webPath = itemVideoInfo.getVideoUrl();
        return webPath;
    }

    private void setCurrentState(int currentState) {
        this.mCurrentState = currentState;
    }
}
