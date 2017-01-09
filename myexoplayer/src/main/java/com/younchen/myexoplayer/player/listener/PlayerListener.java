package com.younchen.myexoplayer.player.listener;

import com.google.android.exoplayer2.text.Cue;

import java.util.List;

/**
 * Created by 龙泉 on 2016/12/27.
 * used to pass player event to player owner
 */

public interface PlayerListener {

    void onLoading(boolean isLoading);

    void onPlayEnd();

    void onStartPlay();

    void onError(Exception error);

    void onPausePlay();

    void onSubtitleOutput(List<Cue> cues);

}
