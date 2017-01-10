package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.exoplayersample.R;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.younchen.myexoplayer.player.listener.VideoControlListener;
import com.example.exoplayersample.video.utils.TimeUtils;
import com.google.android.exoplayer2.text.Cue;
import com.younchen.myexoplayer.player.view.IPlayerView;

import java.util.List;

/**
 * Created by 龙泉 on 2017/1/2 0002.
 */

public class DefaultPlayerView extends RelativeLayout implements IPlayerView, View.OnClickListener {

    private View mRootView;
    private TextureView mTextureView;
    private SeekBar mVideoProgressBar;

    private TextView mPlayTime;
    private TextView mEndTime;

    private ImageView mPlayBtn;
    private ProgressBar mLoadingBar;

    private VideoControlListener mVideoControlListener;

    private ViewGroup mPlayerContainer;
    private ViewGroup mPlayerView;

    private TextView mSubtitleView;

    private ImageView mFastForwardBtn;
    private ImageView mRewindBtn;
    private AspectRatioFrameLayout mVideoViewContainer;

    private LinearLayout mBottomControllBar;
    private RelativeLayout mTitleLayout;

    private boolean mHasFocus;

    private static final String TAG = "DefaultPlayerView";

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
        mHasFocus = false;
    }

    private void initEvent() {
        mPlayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoControlListener != null) {
                    mVideoControlListener.onPlayClicked();
                }
            }
        });
    }

    private void initView() {
        mRootView = inflate(getContext(), R.layout.layout_default_player_view, this);
        mPlayerContainer = (ViewGroup) findViewById(R.id.player_container);
        mTextureView = (TextureView) findViewById(R.id.texture_view);
        mVideoProgressBar = (SeekBar) findViewById(R.id.progress_bar);
        mVideoProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoControlListener != null) {
                    Log.d(TAG, " on seek");
                    mVideoControlListener.onSeek(seekBar.getProgress());
                }
            }
        });
        mLoadingBar = (ProgressBar) findViewById(R.id.loading_progress);

        mPlayTime = (TextView) findViewById(R.id.txt_play_time);
        mEndTime = (TextView) findViewById(R.id.txt_end_time);

        mPlayTime.setText(TimeUtils.timeToString(0));
        mEndTime.setText(TimeUtils.timeToString(0));

        mPlayBtn = (ImageView) findViewById(R.id.btn_play);
        mLoadingBar.setVisibility(GONE);

        mPlayerView = (RelativeLayout) findViewById(R.id.video_player_view);

        mSubtitleView = (TextView) findViewById(R.id.subtitle_view);
        mFastForwardBtn = (ImageView) findViewById(R.id.btn_fast_forward);
        mRewindBtn = (ImageView) findViewById(R.id.btn_rewind);

        mFastForwardBtn.setOnClickListener(this);
        mRewindBtn.setOnClickListener(this);
        mPlayerContainer.setOnClickListener(this);

        mVideoViewContainer = (AspectRatioFrameLayout) findViewById(R.id.video_view_container);
        //mVideoViewContainer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

        mBottomControllBar = (LinearLayout) findViewById(R.id.control_panel);
        mTitleLayout = (RelativeLayout) findViewById(R.id.layout_title);
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
    public ViewGroup getPlayerViewContainer() {
        return mPlayerContainer;
    }

    @Override
    public void onBuffering() {
        mLoadingBar.setVisibility(VISIBLE);
    }

    @Override
    public void onStartPlay() {
        mLoadingBar.setVisibility(GONE);
        mPlayBtn.setImageResource(R.drawable.exo_controls_pause);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onPausePlay() {
        mPlayBtn.setImageResource(R.drawable.exo_controls_play);
    }

    @Override
    public void onEndPlay() {
        mPlayBtn.setImageResource(R.drawable.exo_controls_play);
    }

    @Override
    public void onTimeChanged(String playTime, String totalTime) {
        mPlayTime.setText(playTime);
        mEndTime.setText(totalTime);
    }

    @Override
    public void onBufferChanged(int currentBuffer) {
    }


    @Override
    public void onProgressChanged(int progress) {
        mVideoProgressBar.setProgress(progress);
    }

    @Override
    public void setControlListener(VideoControlListener videoControlListener) {
        mVideoControlListener = videoControlListener;
    }

    @Override
    public void onReceiveSubtitle(List<Cue> cues) {
        StringBuilder sb = new StringBuilder();
        for (Cue cue : cues) {
            sb.append(cue.text);
        }
        mSubtitleView.setText(sb.toString());
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        if (mVideoViewContainer != null) {
            float aspectRatio = height == 0 ? 1 : (width * pixelWidthHeightRatio) / height;
            mVideoViewContainer.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void onOrientationChanged(boolean isLandSpace) {
        if(isLandSpace){
            mPlayerContainer.setFitsSystemWindows(false);
            mPlayerContainer.requestFitSystemWindows();
        }else{
            mPlayerContainer.setFitsSystemWindows(true);
            mPlayerContainer.requestFitSystemWindows();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fast_forward:
                if (mVideoControlListener != null) {
                    mVideoControlListener.onSpeedUp();
                }
                break;
            case R.id.btn_rewind:
                if (mVideoControlListener != null) {
                    mVideoControlListener.onSpeedDown();
                }
                break;
            case R.id.player_container:
                if (mVideoControlListener != null) {
                    mHasFocus = !mHasFocus;
                    mVideoControlListener.onFocusChanged(mHasFocus);
                }
                if (mHasFocus) {
                    showControlBar();
                } else {
                    hideControlBar();
                }
        }
    }

    private void hideControlBar() {
        mBottomControllBar.setVisibility(GONE);
        mTitleLayout.setVisibility(GONE);
    }

    private void showControlBar() {
        mBottomControllBar.setVisibility(VISIBLE);
        mTitleLayout.setVisibility(VISIBLE);
    }
}
