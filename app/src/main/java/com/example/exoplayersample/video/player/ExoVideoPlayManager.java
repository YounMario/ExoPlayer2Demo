package com.example.exoplayersample.video.player;

import android.graphics.Matrix;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.PlayerFactory;
import com.younchen.myexoplayer.player.listener.PlayerListener;

import java.lang.ref.WeakReference;

/**
 * Created by 龙泉 on 2016/10/19.
 */

public class ExoVideoPlayManager {

    private static final String TAG_PLAY_STATE = "ExoPlayState";
    private Player mExoPlayer;
    private PlayableWindow currentWindow;

    //private VideoPlayListener videoPlayListener;


    public ExoVideoPlayManager() {
        mExoPlayer = PlayerFactory.getDefaultPlayer();
        mExoPlayer.setPlayerListener(new PlayerListener() {
            @Override
            public void onBuffering() {
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
            public void onPreparing() {
                if (currentWindow != null) {
                    currentWindow.updateUiToPrepare();
                    currentWindow.showLoading();
                }
            }
        });
    }


    public void onFocus() {
        if (currentWindow != null) {
            currentWindow.updateUiToFocusState();
        }
    }


    private static class InnerHandler extends Handler {
        private WeakReference<ExoVideoPlayManager> mExoVideoPlayManagerRef;

        public InnerHandler(ExoVideoPlayManager playManager) {
            mExoVideoPlayManagerRef = new WeakReference<>(playManager);
        }


        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }

            final ExoVideoPlayManager exoVideoPlayManager = mExoVideoPlayManagerRef.get();
            if (exoVideoPlayManager == null) {
                return;
            }

            exoVideoPlayManager.handleMessage(msg);
        }
    }

    private void handleMessage(Message msg) {

    }


    public void play() {
        mExoPlayer.play();
    }



    public void stopPlay() {
        Log.i(TAG_PLAY_STATE, "stop videoPlayer");
        mExoPlayer.stop();
        if (currentWindow != null) {
            currentWindow.updateUiToNormalState();
            if (currentWindow.getPlayerView() != null) {
//                VideoPlayerBottomBar bottomBar = (VideoPlayerBottomBar) currentWindow.getPlayerView().findViewById(R.id.video_play_bottom_bar);
//                if (bottomBar != null) {
//                    bottomBar.release();
//                }
            }
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
        mExoPlayer.setPlayUri(uri);
    }

    public long getCurrentSeek() {
        return mExoPlayer == null ? 0 : (long) mExoPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return mExoPlayer.isPlaying();
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (currentWindow == null) {
            return;
        }
        TextureView textureView = (TextureView) currentWindow.getVideoView();

        if (textureView == null) return;
        int viewWidth = textureView.getWidth();
        int viewHeight = textureView.getHeight();
        float scaledX = 1.f * width / viewWidth;
        float scaledY = 1.f * height / viewHeight;
        float scale = Math.max(1 / scaledX, 1 / scaledY);

        // Calculate pivot points, in our case crop from center
        int pivotPointX = viewWidth / 2;
        int pivotPointY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(scaledX * scale, scaledY * scale, pivotPointX, pivotPointY);
        textureView.setTransform(matrix);
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
