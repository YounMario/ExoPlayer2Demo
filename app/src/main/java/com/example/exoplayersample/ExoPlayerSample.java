package com.example.exoplayersample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.exoplayersample.video.player.presenter.DefaultPlayerPresenter;
import com.example.exoplayersample.video.player.presenter.IPlayerView;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.PlayerFactory;
import com.younchen.myexoplayer.player.listener.PlayerListener;


public class ExoPlayerSample extends AppCompatActivity  {

    private Player mPlayer;
    private static final String TAG = "ExoPlayerSample";

    private static String PLAY_URL2 = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";

    private DefaultPlayerPresenter mDefaultPresenter;
    private IPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_sample);
        //create presenter
        mPlayerView = (IPlayerView) findViewById(R.id.player_view);
        mPlayer = PlayerFactory.getDefaultPlayer();
        mDefaultPresenter = new DefaultPlayerPresenter(mPlayer, mPlayerView);
        mDefaultPresenter.getPlayer().setPlayUri(Uri.parse(PLAY_URL2)).setPlayerListener(new PlayerListener() {
            @Override
            public void onBuffering() {
                Log.d(TAG, "onBuffering");
            }

            @Override
            public void onPlayEnd() {
                Log.d(TAG, "onPlayEnd");
                Uri uri = Uri.parse(getVideoPlayPath());
                mPlayer.setPlayUri(uri);
                mPlayer.play();
            }

            @Override
            public void onStartPlay() {
                Log.d(TAG, "onStatPlay");
            }

            @Override
            public void onError(Exception error) {
                Log.d(TAG, "onError:" + error.getMessage());
            }

            @Override
            public void onPreparing() {

            }
        });
    }

    private String getVideoPlayPath() {
        return PLAY_URL2;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDefaultPresenter.release();
    }
}
