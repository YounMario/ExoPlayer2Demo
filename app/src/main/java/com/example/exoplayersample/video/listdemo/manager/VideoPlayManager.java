package com.example.exoplayersample.video.listdemo.manager;


import com.example.exoplayersample.video.listdemo.PlayableWindow;

/**
 * Created by 龙泉 on 2016/10/19.
 */

public interface VideoPlayManager {

    void play();

    void stopPlay();

    void pause();

    PlayableWindow getCurrentPlayableWindow();

    void resume();

    void onScrollFinished(boolean isUp);

    void release();

    void onAttach(PlayableWindow needPlayWindow);

    void onDetach(PlayableWindow currentPlayableWindow);

    boolean isPlayState();

    boolean isPauseState();

}
