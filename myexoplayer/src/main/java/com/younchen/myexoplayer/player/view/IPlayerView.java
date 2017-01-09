package com.younchen.myexoplayer.player.view;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.text.Cue;
import com.younchen.myexoplayer.player.listener.PlayerCallBack;
import com.younchen.myexoplayer.player.listener.VideoControlListener;

import java.util.List;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public interface IPlayerView extends PlayerCallBack {

    View getVideoView();

    View getPlayerView();

    ViewGroup getPlayerViewContainer();

    void setControlListener(VideoControlListener videoControlListener);

    void onReceiveSubtitle(List<Cue> cues);

    void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio);
}
