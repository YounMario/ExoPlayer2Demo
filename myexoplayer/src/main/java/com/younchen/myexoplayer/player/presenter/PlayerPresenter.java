package com.younchen.myexoplayer.player.presenter;

import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.view.IPlayerView;

/**
 * Created by 龙泉 on 2016/12/29.
 */

public interface PlayerPresenter {

    void setPlayer(Player player);

    IPlayerView getPlayerView();

    Player getPlayer();

    void release();
}
