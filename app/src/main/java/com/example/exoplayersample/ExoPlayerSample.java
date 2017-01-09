package com.example.exoplayersample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.exoplayersample.video.player.presenter.DefaultPlayerPresenter;
import com.example.exoplayersample.video.player.presenter.IPlayerView;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.PlayerFactory;


public class ExoPlayerSample extends AppCompatActivity  {

    private Player mPlayer;
    private static final String TAG = "ExoPlayerSample";

    private DefaultPlayerPresenter mDefaultPresenter;
    private IPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_sample);
        //create presenter
        mPlayerView = (IPlayerView) findViewById(R.id.player_view);
        mPlayer = PlayerFactory.getDefaultPlayer();
        mDefaultPresenter = new DefaultPlayerPresenter(this, mPlayer, mPlayerView);
        mDefaultPresenter.getPlayer().setVideoUri(Uri.parse("/android_asset/video_demo.mp4"))
                .setSubTitleUri(Uri.parse("/android_asset/demo_subtitle.srt"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDefaultPresenter.release();
    }
}
