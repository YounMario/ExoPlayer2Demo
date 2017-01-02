package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.player.presenter.IPlayerView;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public class DefaultPlayerView extends RelativeLayout implements IPlayerView {

    private View mRootView;
    private SurfaceView mTextureView;
    private VideoProgressBar mVideoProgressBar;

    public DefaultPlayerView(Context context) {
        this(context, null);
    }

    public DefaultPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRootView = inflate(getContext(), R.layout.layout_default_player_view, this);
        mTextureView = (SurfaceView) findViewById(R.id.texture_view);
        mVideoProgressBar = (VideoProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public View getVideoView() {
        return mTextureView;
    }

    @Override
    public ViewGroup getVideoContainer() {
        return (ViewGroup) mRootView;
    }

    @Override
    public View getProgressBar() {
        return mVideoProgressBar;
    }

    @Override
    public void onBuffering() {

    }

    @Override
    public void onStartPlay() {

    }

    @Override
    public void onError() {

    }
}
