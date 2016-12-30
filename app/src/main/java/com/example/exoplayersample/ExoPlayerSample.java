package com.example.exoplayersample;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.younchen.myexoplayer.player.PlayerFactory;
import com.younchen.myexoplayer.player.Player;
import com.younchen.myexoplayer.player.listener.PlayerListener;


public class ExoPlayerSample extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private Player mPlayer;

    private Surface mSurface;
    private static final String TAG = "ExoPlayerSample";

    private static String PLAY_URL2 = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_sample);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);
        mPlayer = PlayerFactory.getDefaultPlayer();

        Uri uri = Uri.parse(getVideoPlayPath());
        mPlayer.setPlayUri(uri);
        mPlayer.setPlayerListener(new PlayerListener() {
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
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if (mSurface != null) {
            mSurface.release();
        }
        mSurface = new Surface(surfaceTexture);
        mPlayer.setSurface(mSurface);
        mPlayer.play();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mSurface != null) {
            mSurface.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}
