package com.example.exoplayersample.video.player.presenter;

import android.view.View;
import android.view.ViewGroup;

import com.example.exoplayersample.video.player.listener.VideoControlListener;
import com.google.android.exoplayer2.text.Cue;

import java.util.List;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public interface IPlayerView extends IPlayerListener {

    View getVideoView();

    View getPlayerView();

    ViewGroup getPlayerViewContainer();

    void setControlListener(VideoControlListener videoControlListener);

    void onReceiveSubtitle(List<Cue> cues);
}
