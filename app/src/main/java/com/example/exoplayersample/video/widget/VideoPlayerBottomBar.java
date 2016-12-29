package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.utils.ContextUtil;
import com.younchen.myexoplayer.player.Player;

import java.lang.ref.WeakReference;

/**
 * Created by 龙泉 on 2016/12/16.
 */

public class VideoPlayerBottomBar extends RelativeLayout implements View.OnClickListener{

    private VideoProgressBar mVideoProgressBar;
    private TextView mStartTime;
    private TextView mEndTime;

    private WeakReference<Player> mPlayerReference;
    private ImageView mBtnFullScreen;
    private int mScreenMode;

    private View mRootView;

    private static final int FULL_SCREEN_MODE = 0;
    private static final int NORMAL_SCREEN_MODE = 1;


    public VideoPlayerBottomBar(Context context) {
        this(context, null);
    }

    public VideoPlayerBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VideoPlayerBottomBar);
        mScreenMode = array.getInt(R.styleable.VideoPlayerBottomBar_change_window_mode, FULL_SCREEN_MODE);
        array.recycle();
        init();
    }


    public void setupPlayer(Player player) {
        mPlayerReference = new WeakReference<>(player);
        mVideoProgressBar.setController(mPlayerReference);
        mVideoProgressBar.setSeekListener(new VideoProgressBar.SeekListener() {
            @Override
            public void onSeek(int progress) {
                seekTo(progress);
            }
        });
    }

    public void release() {
        if (mPlayerReference != null) {
            mPlayerReference.clear();
            mPlayerReference = null;
        }
    }

    private void init() {
        mRootView = inflate(getContext(), R.layout.layout_controll_layout, this);
        mStartTime = (TextView) findViewById(R.id.txt_play_time);
        mEndTime = (TextView) findViewById(R.id.txt_end_time);
        mVideoProgressBar = (VideoProgressBar) findViewById(R.id.progress_bar);
        mBtnFullScreen = (ImageView) findViewById(R.id.btn_change_window_mode);

        mVideoProgressBar.setProgressListener(new VideoProgressBar.ProgressListener() {
            @Override
            public void updateProgress(int progress) {
                updateTextView();
            }
        });
        mEndTime.setText(timeToString(0));
        mStartTime.setText(timeToString(0));
        mBtnFullScreen.setOnClickListener(this);
    }

    private void seekTo(int progress){
        if (mPlayerReference != null && mPlayerReference.get() != null) {
            final Player player = mPlayerReference.get();
            long duringTime = player.getDuration();
            long position = (long) ((duringTime * progress * 1.0) / 100);
            player.seekTo(position);
        }
    }


    private void updateTextView() {
        if (mPlayerReference != null && mPlayerReference.get() != null) {
            long duringTime = mPlayerReference.get().getDuration();
            long currentPos = (long) mPlayerReference.get().getCurrentPosition();
            mEndTime.setText(timeToString(duringTime));
            mStartTime.setText(timeToString(currentPos));
        }
    }

    private String timeToString(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        int minutes = (int) totalSeconds / 60;
        int seconds = (int) totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void setBufferBarVisible(boolean needShow) {
        if (mVideoProgressBar != null) {
            mVideoProgressBar.setNeedShowBufferBar(needShow);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_window_mode:
                changeWindowMode();
                break;
        }
    }

    private void changeWindowMode() {
        if (mScreenMode == FULL_SCREEN_MODE) {
            changeToFullMode();
        } else if (mScreenMode == NORMAL_SCREEN_MODE) {
            changeToNormalMode();
        }
    }

    private void changeToNormalMode() {
        //1.get root window
        ViewGroup rootView = (ViewGroup) ContextUtil.scanForActivity(getContext())
                .findViewById(Window.ID_ANDROID_CONTENT);
        //2.remove full mode view
        View oldScreenView = rootView.findViewById(R.id.full_screen_view);
        if(oldScreenView != null){
            rootView.removeView(oldScreenView);
        }
    }

    private void changeToFullMode() {
        //1.get root window
        ViewGroup rootView = (ViewGroup) ContextUtil.scanForActivity(getContext())
                .findViewById(Window.ID_ANDROID_CONTENT);
        //2.try remove old full screen view
        View oldScreenView = rootView.findViewById(R.id.full_screen_view);
        if(oldScreenView != null){
            rootView.removeView(oldScreenView);
        }
        //3.inflate full screen view
        FullScreenPlayerView fullScreenPlayerView = new FullScreenPlayerView(getContext());
        fullScreenPlayerView.setId(R.id.full_screen_view);
        //3. add full screen view to root window
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Player player = mPlayerReference.get();
        player.pause();
        fullScreenPlayerView.setPlayer(player);
        rootView.addView(fullScreenPlayerView, lp);
    }

}
