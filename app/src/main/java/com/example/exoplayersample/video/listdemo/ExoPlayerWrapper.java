package com.example.exoplayersample.video.listdemo;

import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.text.Cue;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.PlayerFactory;
import com.younchen.myexoplayer.player.listener.PlayerListener;

import java.util.List;

/**
 * Created by 龙泉 on 2016/10/19.
 */

public class ExoPlayerWrapper {

    private static final String TAG_PLAY_STATE = "ExoPlayState";
    private Player mExoPlayer;
    private PlayableWindow currentWindow;



    public ExoPlayerWrapper() {
        mExoPlayer = PlayerFactory.getDefaultPlayer();
        mExoPlayer.setPlayerListener(new PlayerListener() {
            @Override
            public void onLoading(boolean isLoading) {
                if (currentWindow != null) {
                    currentWindow.showLoading();
                }
            }

            @Override
            public void onPlayEnd() {

            }

            @Override
            public void onStartPlay() {
                if (currentWindow != null) {
                    currentWindow.hideLoading();
                    currentWindow.updateUiToPlayState();
                }
            }

            @Override
            public void onError(Exception error) {

            }

            @Override
            public void onPausePlay() {

            }

            @Override
            public void onSubtitleOutput(List<Cue> cues) {

            }
        });
    }


    public void onFocus() {
        if (currentWindow != null) {
            currentWindow.updateUiToFocusState();
        }
    }


    public void play() {
        mExoPlayer.play();
    }

    public Player getPlayer(){
        return mExoPlayer;
    }


    public void stopPlay() {
        Log.i(TAG_PLAY_STATE, "stop videoPlayer");
        mExoPlayer.stop();
        if (currentWindow != null) {
            currentWindow.updateUiToNormalState();
        }
    }

    public void pause() {
        Log.i(TAG_PLAY_STATE, "pause videoPlayer");
        if (mExoPlayer != null) {
            mExoPlayer.pause();
        }

        if (currentWindow != null) {
            currentWindow.updateUiToPauseState();
        }
    }


    public void seekTo(int position) {
        if (mExoPlayer != null) {
            mExoPlayer.seekTo(position);
        }
    }


    public void setPlayableWindow(PlayableWindow window) {
        this.currentWindow = window;
    }


    public void resume() {
        if (mExoPlayer != null) {
            mExoPlayer.resume();
        }
        if (currentWindow != null) {
            currentWindow.updateUiToPlayState();
        }
    }


    public void setUrl(String url) {
        Uri uri = Uri.parse(url);
        mExoPlayer.setVideoUri(uri);
    }

    public long getCurrentSeek() {
        return mExoPlayer == null ? 0 : (long) mExoPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return mExoPlayer.isPlaying();
    }


    public void release() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setSurface(Surface surface) {
        if (mExoPlayer != null) {
            mExoPlayer.setSurface(surface);
        }
    }

}
