package com.example.exoplayersample.video.listdemo.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.exoplayersample.video.listdemo.PlayableWindow;
import com.example.exoplayersample.video.listdemo.manager.VideoPlayManager;

import java.util.ArrayList;

/**
 * Created by 龙泉 on 2016/10/19.
 */

public class PlayWindowScrollerListener extends RecyclerView.OnScrollListener {


    private VideoPlayManager mPlayManager;
    private ArrayList<PlayableWindow> mPlayableWindows;
    private boolean mLastScroll;

    public PlayWindowScrollerListener(VideoPlayManager playManager) {
        this.mPlayManager = playManager;
        mPlayableWindows = new ArrayList<>();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        mLastScroll = dy > 0;
    }

    private PlayableWindow findNeedPlayWindow(RecyclerView recyclerView) {
        int firstPosition = RecyclerView.NO_POSITION;
        int lastPosition = RecyclerView.NO_POSITION;

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
            lastPosition = linearLayoutManager.findLastVisibleItemPosition();
        }
        //枚举第一次展现的item 到最后一次展现的item的播放权重
        for (int i = firstPosition; i <= lastPosition; i++) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
            if (holder instanceof PlayableWindow) {
                PlayableWindow playerWindow = (PlayableWindow) holder;
                //update all visible item position
                playerWindow.setWindowIndex(i);
                if (playerWindow.canPlay()) {
                    mPlayableWindows.add(playerWindow);
                }
            }
        }

        return findNeedPlayWindowInArray(lastPosition, recyclerView.getAdapter().getItemCount());
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }
        mPlayManager.onScrollFinished(mLastScroll);
        mPlayableWindows.clear();
        PlayableWindow currentPlayableWindow = mPlayManager.getCurrentPlayableWindow();
        PlayableWindow needPlayWindow = findNeedPlayWindow(recyclerView);

        if (isWindowIndexNotChanged(needPlayWindow, currentPlayableWindow) && isPlayWindowInstanceNotChanged(needPlayWindow, currentPlayableWindow)) {
            mPlayManager.onAttach(needPlayWindow);
            return;
        }

        if (currentPlayableWindow != null) {
            mPlayManager.onDetach(currentPlayableWindow);
        }

        if (needPlayWindow == null) {
            return;
        }
        mPlayManager.onAttach(needPlayWindow);
    }


    private PlayableWindow findNeedPlayWindowInArray(int lastPosition, int recycleViewItemCount) {
        if (mPlayableWindows.size() == 0) {
            return null;
        }
        int lastIndex = mPlayableWindows.size() - 1;
        PlayableWindow lastPlayableWindow = mPlayableWindows.get(lastIndex);

        if (lastIndex == 0 || lastPosition != recycleViewItemCount - 1 || lastPlayableWindow.getWindowIndex() < lastPosition) {
            return mPlayableWindows.get(0);
        }

        PlayableWindow secondLastPlayableWindow = mPlayableWindows.get(lastIndex - 1);

        if (secondLastPlayableWindow != null) {
            if (lastPlayableWindow.getWindowLastCalculateArea() < secondLastPlayableWindow.getWindowLastCalculateArea()) {
                return secondLastPlayableWindow;
            }
        }
        return lastPlayableWindow;
    }

    private boolean isWindowIndexNotChanged(PlayableWindow needPlayWindow, PlayableWindow currentPlayableWindow) {
        return needPlayWindow != null && currentPlayableWindow != null && needPlayWindow.getWindowIndex() == currentPlayableWindow.getWindowIndex();
    }

    private boolean isPlayWindowInstanceNotChanged(PlayableWindow window1, PlayableWindow window2) {
        return window1 == window2;
    }
}