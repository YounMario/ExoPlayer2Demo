package com.example.exoplayersample.video.player.presenter;

import android.view.View;

import com.younchen.myexoplayer.player.Player;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public interface PlayerPresenter {

    void setPlayer(Player player);

    void setPlayerView(View view);

    View getPlayerView();

    Player getPlayer();

    void saveParentView(View parent);

    void restoreLastParentView();

}
