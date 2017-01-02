package com.example.exoplayersample.video.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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

public class DefaultPlayerView extends RelativeLayout implements IPlayerView {

    private View mRootView;
    private SurfaceView mTextureView;
    private VideoProgressBar mVideoProgressBar;

    private TextView mPlayTime;
    private TextView mEndTime;

    private ImageView mPlayBtn;
    private ImageView mLoading;

    private VideoControlListener mVideoControlListener;
    private ObjectAnimator mRotateAnimation;


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
        mTextureView = (SurfaceView) findViewById(R.id.texture_view);
        mVideoProgressBar = (VideoProgressBar) findViewById(R.id.progress_bar);
        mLoading = (ImageView) findViewById(R.id.img_loading);

        mPlayTime = (TextView) findViewById(R.id.txt_play_time);
        mEndTime = (TextView) findViewById(R.id.txt_end_time);

        mPlayTime.setText(TimeUtils.timeToString(0));
        mEndTime.setText(TimeUtils.timeToString(0));

        mPlayBtn = (ImageView) findViewById(R.id.btn_play);
        mLoading.setVisibility(GONE);

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

}
