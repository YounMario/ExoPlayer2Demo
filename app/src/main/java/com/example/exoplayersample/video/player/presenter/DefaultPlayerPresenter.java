package com.example.exoplayersample.video.player.presenter;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.example.exoplayersample.video.player.listener.VideoControlListener;
import com.example.exoplayersample.video.utils.TimeUtils;
import com.example.exoplayersample.video.widget.VideoProgressBar;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.listener.PlayerListener;


/**
 * Created by 龙泉 on 2016/12/29.
 */

public class DefaultPlayerPresenter implements PlayerPresenter, TextureView.SurfaceTextureListener, SurfaceHolder.Callback {

    private static final String TAG = "DefaultPlayerPresenter";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_PAUSE = 1;
    private static final int STATE_PLAY = 2;

    private Player mPlayer;
    private IPlayerView mPlayerView;

    private ViewGroup mLastParent;
    private ViewGroup.LayoutParams mLastViewParams;

    private Surface mSurface;

    private int mCurrentState;

    public DefaultPlayerPresenter(Player player, IPlayerView playerView) {
        mPlayer = player;
        mPlayerView = playerView;
        checkPlayer();
        initData();
        setEventListener();
    }

    private void checkPlayer() {
        if(mPlayer == null){
            throw new RuntimeException("player is null");
        }
        if(mPlayerView == null){
            throw new RuntimeException("player view is null");
        }
    }

    private void initData() {
        setCurrentState(STATE_NORMAL);
    }

    private void setEventListener() {
        setDisPlayListener();
        setStateListener();
        setControlListener();
        setupProgressBar();
    }

    private void setupProgressBar() {
        View progressBar = mPlayerView.getProgressBar();
        //here exoplayer not presented callback for progress
        //so get player current progress on custom view onDraw function
        if (progressBar instanceof VideoProgressBar) {
            VideoProgressBar videoProgressBar = (VideoProgressBar) progressBar;
            videoProgressBar.setController(mPlayer);
        }
    }

    private void setControlListener() {
        mPlayerView.setControlListener(new VideoControlListener() {
            @Override
            public void onPlayClicked() {

                switch (mCurrentState){
                    case STATE_NORMAL:
                        mPlayer.play();
                        setCurrentState(STATE_PLAY);
                        break;
                    case STATE_PLAY:
                        mPlayer.pause();
                        setCurrentState(STATE_PAUSE);
                        break;
                    case STATE_PAUSE:
                        setCurrentState(STATE_PLAY);
                        mPlayer.resume();
                        break;
                    default:
                        setCurrentState(STATE_PLAY);
                        mPlayer.play();
                }
            }

            @Override
            public void onSeek(int progress) {
                long seekTime = (long) (progress * 1.0 * mPlayer.getDuration() /100);
                mPlayer.seekTo(seekTime);
            }

            @Override
            public void onProgress(int progress) {
                //not good solution .
                mPlayerView.onTimeChanged(TimeUtils.timeToString((long) mPlayer.getCurrentPosition()), TimeUtils.timeToString(mPlayer.getDuration()));
            }

            @Override
            public void onEnterFullScreenMode() {
                changeToFullScreenMode();
            }

            @Override
            public void onQuitFullScreenMode() {

            }
        });
    }

    private void changeToFullScreenMode() {

    }

    private void setCurrentState(int state) {
        mCurrentState = state;
    }

    private void setStateListener() {
        mPlayer.setPlayerListener(new PlayerListener() {
            @Override
            public void onLoading(boolean isLoading) {
                if(isLoading){
                    mPlayerView.onBuffering();
                }
            }

            @Override
            public void onPlayEnd() {
                Log.d(TAG, "onPlayEnd");
                setCurrentState(STATE_NORMAL);
            }

            @Override
            public void onStartPlay() {
                Log.d(TAG, "onStartPlay");
                mPlayerView.onStartPlay();
                setCurrentState(STATE_PLAY);
            }

            @Override
            public void onError(Exception error) {
                Log.d(TAG, "onError :" + error.getMessage());
                mPlayerView.onError();
            }

            @Override
            public void onPreparing() {
                Log.d(TAG, "onPreparing");
            }

            @Override
            public void onPausePlay() {
                Log.d(TAG, "onPausePlay");
                mPlayerView.onPausePlay();
                setCurrentState(STATE_PAUSE);
            }
        });
    }

    private void setDisPlayListener() {
        View videoDisplayView = mPlayerView.getVideoView();
        if (videoDisplayView instanceof TextureView) {
            TextureView textureView = (TextureView) videoDisplayView;
            textureView.setSurfaceTextureListener(this);
        } else if (videoDisplayView instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) videoDisplayView;
            surfaceView.getHolder().addCallback(this);
        }
    }


    @Override
    public void setPlayer(Player player) {
        mPlayer = player;
    }

    @Override
    public void setPlayerView(IPlayerView view) {
        mPlayerView = view;
    }

    @Override
    public IPlayerView getPlayerView() {
        return mPlayerView;
    }

    @Override
    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    public void saveCurrentPlayerParent() {
        if (mPlayerView != null) {
            ViewGroup parent = (ViewGroup) mPlayerView.getVideoView().getParent();
            if (parent != null) {
                mLastViewParams = parent.getLayoutParams();
                mLastParent = parent;
            }
        }
    }

    @Override
    public void restoreLastParentView() {
        if (mPlayer != null && mLastParent != null) {
            ViewGroup viewParent = (ViewGroup) mPlayerView.getVideoView().getParent();
            if (viewParent != null) {
                viewParent.removeView(mPlayerView.getVideoView());
            }
            mLastParent.addView(mPlayerView.getVideoView(), mLastViewParams);
        }
    }

    @Override
    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    //============================== for texture view==============================
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        mSurface = new Surface(surfaceTexture);
        mPlayer.setSurface(mSurface);
        mPlayer.play();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    //============================== for texture view==============================


    //============================== for surface view==============================
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        mSurface = surfaceHolder.getSurface();
        mPlayer.setSurface(mSurface);
        mPlayer.play();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
    }

    //============================== for surface view==============================
}
