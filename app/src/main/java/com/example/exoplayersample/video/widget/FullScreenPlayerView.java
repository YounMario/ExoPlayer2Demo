package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.exoplayersample.R;
import com.younchen.myexoplayer.player.Player;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public class FullScreenPlayerView extends RelativeLayout implements TextureView.SurfaceTextureListener ,View.OnClickListener{

    private View mRootView;
    private TextureView mTextureView;
    private Surface mSurface;

    private Player mPlayer;
    private VideoPlayerBottomBar mVideoPlayBottomBar;

    public FullScreenPlayerView(Context context) {
        this(context, null);
    }

    public FullScreenPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullScreenPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRootView = inflate(getContext(), R.layout.layout_full_screen_player, this);
        mTextureView = (TextureView) mRootView.findViewById(R.id.texture_view);
        mVideoPlayBottomBar = (VideoPlayerBottomBar) mRootView.findViewById(R.id.video_play_bottom_bar);
        mTextureView.setSurfaceTextureListener(this);
        setOnClickListener(this);
    }

    public void setPlayer(Player player) {
        this.mPlayer = player;
        if (mPlayer != null) {
            mVideoPlayBottomBar.setupPlayer(mPlayer);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mSurface != null) {
            mSurface.release();
        }
        mSurface = new Surface(surface);
        mPlayer.setSurface(mSurface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mSurface != null) {
            mSurface.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View view) {

    }
}
