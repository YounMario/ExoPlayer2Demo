package com.example.exoplayersample.video.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.listdemo.anim.AnimationUtils;
import com.example.exoplayersample.video.player.listener.VideoControlListener;
import com.example.exoplayersample.video.player.presenter.IPlayerView;
import com.example.exoplayersample.video.utils.TimeUtils;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public class DefaultPlayerView extends RelativeLayout implements IPlayerView, View.OnClickListener {

    private View mRootView;
    private TextureView mTextureView;
    private VideoProgressBar mVideoProgressBar;

    private TextView mPlayTime;
    private TextView mEndTime;

    private ImageView mPlayBtn;
    private ImageView mLoading;
    private ImageView mFullScreenBtn;

    private VideoControlListener mVideoControlListener;
    private ObjectAnimator mRotateAnimation;

    private boolean mIsFullScreen;
    private ViewGroup mPlayerContainer;
    private ViewGroup mPlayerView;


    public DefaultPlayerView(Context context) {
        this(context, null);
    }

    public DefaultPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initEvent();
        initData();
    }

    private void initData() {
        mIsFullScreen = false;
    }

    private void initEvent() {
        mVideoProgressBar.setProgressListener(new VideoProgressBar.ProgressListener() {
            @Override
            public void updateProgress(int progress) {
                mVideoControlListener.onProgress(progress);
            }
        });

        mVideoProgressBar.setSeekListener(new VideoProgressBar.SeekListener() {
            @Override
            public void onSeek(int progress) {
                mVideoControlListener.onSeek(progress);
            }
        });

        mPlayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoControlListener.onPlayClicked();
            }
        });


    }

    private void initView() {
        mRootView = inflate(getContext(), R.layout.layout_default_player_view, this);
        mPlayerContainer = (ViewGroup) findViewById(R.id.player_container);
        mTextureView = (TextureView) findViewById(R.id.texture_view);
        mVideoProgressBar = (VideoProgressBar) findViewById(R.id.progress_bar);
        mLoading = (ImageView) findViewById(R.id.img_loading);

        mPlayTime = (TextView) findViewById(R.id.txt_play_time);
        mEndTime = (TextView) findViewById(R.id.txt_end_time);

        mPlayTime.setText(TimeUtils.timeToString(0));
        mEndTime.setText(TimeUtils.timeToString(0));

        mPlayBtn = (ImageView) findViewById(R.id.btn_play);
        mLoading.setVisibility(GONE);

        mPlayerView = (RelativeLayout) findViewById(R.id.video_player_view);

        mFullScreenBtn = (ImageView) findViewById(R.id.btn_full_screen);
        mFullScreenBtn.setOnClickListener(this);
        mFullScreenBtn.setImageResource(R.drawable.icon_full_screen);
    }


    @Override
    public View getVideoView() {
        return mTextureView;
    }

    @Override
    public ViewGroup getPlayerView() {
        return mPlayerView;
    }


    @Override
    public View getProgressBar() {
        return mVideoProgressBar;
    }

    @Override
    public ViewGroup getPlayerViewContainer() {
        return mPlayerContainer;
    }

    @Override
    public void onBuffering() {
        startLoading();
    }

    @Override
    public void onStartPlay() {
        stopLoading();
        mPlayBtn.setImageResource(R.drawable.icon_pause);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onPausePlay() {
        mPlayBtn.setImageResource(R.drawable.icon_play);
    }

    @Override
    public void onTimeChanged(String playTime, String totalTime) {
        mPlayTime.setText(playTime);
        mEndTime.setText(totalTime);
    }

    @Override
    public void onFullScreenMode() {
        mIsFullScreen = true;
        mFullScreenBtn.setImageResource(R.drawable.icon_normal_screen);
        if (mVideoControlListener != null) {
            mVideoControlListener.onEnterFullScreenMode();
        }
    }

    @Override
    public void onQuitFullScreenMode() {
        mIsFullScreen = false;
        mFullScreenBtn.setImageResource(R.drawable.icon_full_screen);
        if (mVideoControlListener != null) {
            mVideoControlListener.onQuitFullScreenMode();
        }
    }

    @Override
    public void setControlListener(VideoControlListener videoControlListener) {
        mVideoControlListener = videoControlListener;
    }

    private void startLoading() {
        if (mLoading != null) {
            mLoading.setVisibility(View.VISIBLE);
            startRotateAnimation();
        }
    }

    private void stopLoading() {
        if (mLoading != null) {
            mLoading.setVisibility(View.GONE);
            stopRotateAnimation();
        }
    }

    private void startRotateAnimation() {
        if (mRotateAnimation != null) {
            mRotateAnimation.cancel();
            mRotateAnimation = null;
        }
        mRotateAnimation = AnimationUtils.createRotationAnimation(mLoading);
        mRotateAnimation.start();
    }

    private void stopRotateAnimation() {
        if (mRotateAnimation != null) {
            mRotateAnimation.cancel();
            mRotateAnimation = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_full_screen:
                if (mIsFullScreen) {
                    onQuitFullScreenMode();
                } else {
                    onFullScreenMode();
                }
                break;
        }
    }
}
