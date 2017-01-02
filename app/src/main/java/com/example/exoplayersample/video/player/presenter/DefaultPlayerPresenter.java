package com.example.exoplayersample.video.player.presenter;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.listener.PlayerListener;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public class DefaultPlayerPresenter implements PlayerPresenter, TextureView.SurfaceTextureListener,SurfaceHolder.Callback  {

    private Player mPlayer;
    private IPlayerView mPlayerView;

    private ViewGroup mLastParent;
    private ViewGroup.LayoutParams mLastViewParams;

    private Surface mSurface;

    public DefaultPlayerPresenter(Player player, IPlayerView playerView) {
        mPlayer = player;
        mPlayerView = playerView;
        setDisPlayListener();
        setStateListener();
        setControlListener();
    }

    private void setControlListener() {

    }

    private void setStateListener() {
        mPlayer.setPlayerListener(new PlayerListener() {
            @Override
            public void onBuffering() {
                mPlayerView.onBuffering();
            }

            @Override
            public void onPlayEnd() {

            }

            @Override
            public void onStartPlay() {
                mPlayerView.onStartPlay();
            }

            @Override
            public void onError(Exception error) {
                mPlayerView.onError();
            }

            @Override
            public void onPreparing() {

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
        if(mPlayer != null){
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
        if(mSurface != null){
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
        if(mSurface != null){
            mSurface.release();
            mSurface = null;
        }
    }

    //============================== for surface view==============================
}
