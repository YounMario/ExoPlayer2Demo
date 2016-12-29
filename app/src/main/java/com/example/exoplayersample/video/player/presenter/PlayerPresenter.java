package com.example.exoplayersample.video.player.presenter;

import android.view.View;
import android.view.ViewGroup;

import com.younchen.myexoplayer.player.Player;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public interface PlayerPresenter {

    void setPlayer(Player player);

    void setPlayerView(IPlayerView view);

    IPlayerView getPlayerView();

    Player getPlayer();

    void saveCurrentPlayerParent();

    void restoreLastParentView();

}
