package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exoplayersample.R;
import com.younchen.myexoplayer.player.Player;

import java.lang.ref.WeakReference;

/**
 * Created by 龙泉 on 2016/12/16.
 */

public class VideoPlayerBottomBar extends RelativeLayout {

    private VideoProgressBar mVideoProgressBar;
    private TextView mStartTime;
    private TextView mEndTime;

    private WeakReference<Player> mPlayerReference;

    public VideoPlayerBottomBar(Context context) {
        this(context, null);
    }

    public VideoPlayerBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mStartTime = (TextView) findViewById(R.id.txt_play_time);
        mEndTime = (TextView) findViewById(R.id.txt_end_time);
        mVideoProgressBar = (VideoProgressBar) findViewById(R.id.progress_bar);
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
        mVideoProgressBar.setProgressListener(new VideoProgressBar.ProgressListener() {
            @Override
            public void updateProgress(int progress) {
                updateTextView();
            }
        });
        mEndTime.setText(timeToString(0));
        mStartTime.setText(timeToString(0));
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
}
