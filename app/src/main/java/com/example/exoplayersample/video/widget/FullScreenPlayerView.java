package com.example.exoplayersample.video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.player.presenter.PlayerPresenter;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public class FullScreenPlayerView extends RelativeLayout {

    private View mRootView;

    private PlayerPresenter mPlayer;
    private VideoPlayerBottomBar mVideoPlayBottomBar;
    private FrameLayout mFrameLayout;

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
        mVideoPlayBottomBar = (VideoPlayerBottomBar) mRootView.findViewById(R.id.video_play_bottom_bar);
        mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.play_view_container);
    }

    public void setPlayerPresenter(PlayerPresenter player) {
        this.mPlayer = player;
        if (mPlayer != null) {
            mVideoPlayBottomBar.setupPlayerPresenter(mPlayer);
        }
        View playerView = player.getPlayerView().getVideoView();
        ViewGroup oldParent = (ViewGroup) playerView.getParent();
        if (oldParent != null) {
            oldParent.removeView(playerView);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mFrameLayout.addView(playerView, params);
    }

}
