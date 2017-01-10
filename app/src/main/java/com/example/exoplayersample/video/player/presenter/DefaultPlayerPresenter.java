package com.example.exoplayersample.video.player.presenter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.utils.SystemUiUtils;
import com.younchen.myexoplayer.player.listener.VideoControlListener;
import com.example.exoplayersample.video.utils.ContextUtil;
import com.example.exoplayersample.video.utils.TimeUtils;
import com.google.android.exoplayer2.text.Cue;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.listener.PlayerListener;
import com.younchen.myexoplayer.player.presenter.PlayerPresenter;
import com.younchen.myexoplayer.player.view.IPlayerView;

import java.util.List;


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

    private Surface mSurface;

    private int mCurrentState;
    private Context mContext;

    private Handler mUiHandler;

    private static final float MAX_SPEED = 4.0f;
    private static final float MIN_SPEED = 0.25f;
    private static final float SPEED_INCREASE = 0.25f;


    public DefaultPlayerPresenter(Context context, Player player, IPlayerView playerView) {
        mPlayer = player;
        mPlayerView = playerView;
        mContext = context;
        checkPlayer();
        initView();
        initData();
        setEventListener();
    }

    private void initView() {
        setTransParentBar();
        hideSystemUI();
    }

    private void checkPlayer() {
        if (mPlayer == null) {
            throw new RuntimeException("player is null");
        }
        if (mPlayerView == null) {
            throw new RuntimeException("player view is null");
        }
    }

    private void initData() {
        setCurrentState(STATE_NORMAL);
        mUiHandler = new Handler();
    }

    private void setEventListener() {
        setDisPlayListener();
        setStateListener();
        setControlListener();
    }


    private void setControlListener() {
        mPlayerView.setControlListener(new VideoControlListener() {
            @Override
            public void onPlayClicked() {

                switch (mCurrentState) {
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
                long seekTime = (long) (progress * 1.0 * mPlayer.getDuration() / 100);
                mPlayer.seekTo(seekTime);
            }

            @Override
            public void onEnterFullScreenMode() {
                changeToFullScreenMode();
            }

            @Override
            public void onQuitFullScreenMode() {
                changeToNormalScreenMode();
            }

            @Override
            public void onSpeedUp() {
                Log.d(TAG, "speed up:" + mPlayer.getPlaySpeed());
                if (mPlayer.getPlaySpeed() + SPEED_INCREASE <= MAX_SPEED) {
                    mPlayer.setPlaySpeed(mPlayer.getPlaySpeed() + SPEED_INCREASE);
                }
            }

            @Override
            public void onSpeedDown() {
                Log.d(TAG, "speed down :" + mPlayer.getPlaySpeed());
                if (mPlayer.getPlaySpeed() - SPEED_INCREASE >= MIN_SPEED) {
                    mPlayer.setPlaySpeed(mPlayer.getPlaySpeed() - SPEED_INCREASE);
                }
            }

            @Override
            public void onFocusChanged(boolean hasFocus) {
                if (hasFocus) {
                    showSystemUI();
                } else {
                    hideSystemUI();
                }
            }
        });
    }
    //===================================transparent navigation bar===========================
    private void setTransParentBar(){
        SystemUiUtils.setTransParentBar(mContext);
    }
    //===================================transparent navigation bar===========================

    //===================================hide system ui===============================================================
    private void hideSystemUI() {
        SystemUiUtils.hideSystemUI(mContext);
    }

    private void showSystemUI() {
        SystemUiUtils.showSystemUI(mContext);
    }
    //===================================hide system ui===============================================================

    //------------------- full screen mode -----------------------------//
    private void changeToNormalScreenMode() {
        ViewGroup windowView = (ViewGroup) ContextUtil.scanForActivity(mContext)
                .findViewById(Window.ID_ANDROID_CONTENT);

        View view = windowView.findViewById(R.id.full_screen_view);
        if (view != null) {
            windowView.removeView(view);
        }

        View videoView = mPlayerView.getPlayerView();
        removeParent(videoView);
        mPlayerView.getPlayerViewContainer().addView(videoView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void changeToFullScreenMode() {
        ViewGroup windowView = (ViewGroup) ContextUtil.scanForActivity(mContext)
                .findViewById(Window.ID_ANDROID_CONTENT);

        View view = windowView.findViewById(R.id.full_screen_view);
        if (view != null) {
            windowView.removeView(view);
        }

        View fullScreenView = View.inflate(mContext, R.layout.layout_full_screen_player, null);
        View videoView = mPlayerView.getPlayerView();
        removeParent(videoView);
        ViewGroup fullScreenViewContainer = (ViewGroup) fullScreenView.findViewById(R.id.play_view_container);
        fullScreenViewContainer.addView(videoView);
        windowView.addView(fullScreenView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    //------------------- full screen mode -----------------------------//

    private void removeParent(View view) {
        if (view != null) {
            ViewGroup videoViewParent = (ViewGroup) view.getParent();
            if (videoViewParent != null) {
                videoViewParent.removeView(view);
            }
        }
    }

    private void setCurrentState(int state) {
        mCurrentState = state;
    }

    private void setStateListener() {
        mPlayer.setPlayerListener(new PlayerListener() {
            @Override
            public void onLoading(boolean isLoading) {
                if (isLoading) {
                    mPlayerView.onBuffering();
                }
            }

            @Override
            public void onPlayEnd() {
                Log.d(TAG, "onPlayEnd");
                setCurrentState(STATE_NORMAL);
                mPlayerView.onEndPlay();
            }

            @Override
            public void onStartPlay() {
                Log.d(TAG, "onStartPlay");
                mPlayerView.onStartPlay();
                setCurrentState(STATE_PLAY);
                updateProgress();
            }

            @Override
            public void onError(Exception error) {
                Log.d(TAG, "onError :" + error.getMessage());
                mPlayerView.onError();
            }

            @Override
            public void onPausePlay() {
                Log.d(TAG, "onPausePlay");
                mPlayerView.onPausePlay();
                setCurrentState(STATE_PAUSE);
            }

            @Override
            public void onSubtitleOutput(List<Cue> cues) {
                mPlayerView.onReceiveSubtitle(cues);
            }

            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                mPlayerView.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
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
    public IPlayerView getPlayerView() {
        return mPlayerView;
    }

    @Override
    public Player getPlayer() {
        return mPlayer;
    }


    @Override
    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int screenOrientation = newConfig.orientation; //获取屏幕方向
        //change to landscape
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, " change to land");

        }
        //change to portrait
        else if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, " change to line");

        }
    }

    //============================== for texture view==============================
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "width:" + i + " height:" + i1);
        if (mSurface == null) {
            mSurface = new Surface(surfaceTexture);
            mPlayer.setSurface(mSurface);
            mPlayer.play();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "changed width:" + i + " height:" + i1);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mSurface = null;
        mPlayer.release();
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



    //============================== this part can move to player class ===========
    private Runnable mUpdateProgressAction = new UpdateProgress();

    //update Progress action
    private void updateProgress() {
        if (mPlayerView == null || mPlayer == null) {
            return;
        }
        mUiHandler.removeCallbacks(mUpdateProgressAction);
        if(mPlayer.isPlaying()){
            long duration = mPlayer.getDuration();
            long position = mPlayer.getCurrentPosition();
            long bufferPercentage = mPlayer.getBufferedPercentage();
            long playPercentage = (long) (position * 1.0 * 100 / duration);
            mPlayerView.onTimeChanged(TimeUtils.timeToString(position), TimeUtils.timeToString(duration));
            mPlayerView.onBufferChanged((int) bufferPercentage);
            mPlayerView.onProgressChanged((int) playPercentage);
            mUiHandler.postDelayed(mUpdateProgressAction, 500);
        }
    }

    private class UpdateProgress implements Runnable {

        @Override
        public void run() {
            updateProgress();
        }
    }
    //============================== this part can move to player class ===========

}
