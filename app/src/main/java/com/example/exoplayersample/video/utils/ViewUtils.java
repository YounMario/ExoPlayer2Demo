package com.example.exoplayersample.video.utils;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewParent;

import com.example.exoplayersample.video.listdemo.PlayableWindow;


/**
 * Created by 龙泉 on 2016/10/19.
 */

public class ViewUtils {

    public static float visibleArea(PlayableWindow playableWindow, ViewParent parent) {
        if (playableWindow.getPlayerView() == null) {
            throw new IllegalArgumentException("Player must have a valid VideoView.");
        }

        Rect videoRect = getVideoRect(playableWindow);
        Rect parentRect = getRecyclerViewRect(parent);

        if (parentRect != null && (parentRect.contains(videoRect) || parentRect.intersect(videoRect))) {
            float visibleArea = videoRect.height() * videoRect.width();
            float viewArea = playableWindow.getPlayerView().getWidth() * playableWindow.getPlayerView().getHeight();
            return viewArea <= 0.f ? 1.f : visibleArea / viewArea;
        } else {
            return 0.f;
        }
    }

    private static Rect getVideoRect(PlayableWindow player) {
        Rect rect = new Rect();
        Point offset = new Point();
        player.getPlayerView().getGlobalVisibleRect(rect, offset);
        return rect;
    }

    @Nullable
    private static Rect getRecyclerViewRect(ViewParent parent) {
        if (parent == null) { // view is not attached to RecyclerView parent
            return null;
        }

        if (!(parent instanceof View)) {
            return null;
        }

        Rect rect = new Rect();
        Point offset = new Point();
        ((View) parent).getGlobalVisibleRect(rect, offset);
        return rect;
    }
}
