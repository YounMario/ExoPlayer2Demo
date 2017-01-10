package com.example.exoplayersample;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.exoplayersample.video.player.presenter.DefaultPlayerPresenter;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.PlayerFactory;
import com.younchen.myexoplayer.player.view.IPlayerView;


public class ExoPlayerSample extends AppCompatActivity  {

    private Player mPlayer;
    private static final String TAG = "ExoPlayerSample";

    private DefaultPlayerPresenter mDefaultPresenter;
    private IPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_exo_player_sample);
        //create presenter
        mPlayerView = (IPlayerView) findViewById(R.id.player_view);
        mPlayer = PlayerFactory.getDefaultPlayer();
        mDefaultPresenter = new DefaultPlayerPresenter(this, mPlayer, mPlayerView);
        mDefaultPresenter.getPlayer().setVideoUri(Uri.parse("/android_asset/video_demo.mp4"))
                .setSubTitleUri(Uri.parse("/android_asset/demo_subtitle.srt"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDefaultPresenter.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDefaultPresenter.release();
    }

}
