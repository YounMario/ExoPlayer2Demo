package com.example.exoplayersample.video.player.presenter;

import android.view.ViewGroup;

import com.younchen.myexoplayer.player.Player;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public class DefalutPlayerPresenter implements PlayerPresenter{

    private Player mPlayer;
    private IPlayerView mPlayerView;

    private ViewGroup mLastParent;
    private ViewGroup.LayoutParams mLastViewParams;

    public DefalutPlayerPresenter(Player player,IPlayerView playerView){
        mPlayer = player;
        mPlayerView = playerView;
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

}
