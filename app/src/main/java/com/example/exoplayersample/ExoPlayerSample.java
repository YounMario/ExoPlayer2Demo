package com.example.exoplayersample;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.younchen.myexoplayer.player.PlayerFactory;
import com.younchen.myexoplayer.player.PlayerManager;
import com.younchen.myexoplayer.player.listener.PlayerListener;


public class ExoPlayerSample extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private PlayerManager mPlayerManager;

    private Surface mSurface;
    private static final String TAG = "ExoPlayerSample";

    private static String PLAY_URL2 = "http://img.locker.cmcm.com/livelock/uservideo/90f1353176bc83dffe2f246eba496c7a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_sample);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);
        mPlayerManager = PlayerFactory.newInstance();

        mPlayerManager.init();
        Uri uri = Uri.parse(getVideoPlayPath());
        mPlayerManager.setPlayUri(uri);
        mPlayerManager.setPlayerListener(new PlayerListener() {
            @Override
            public void onBuffering() {
                Log.d(TAG, "onBuffering");
            }

            @Override
            public void onPlayEnd() {
                Log.d(TAG, "onPlayEnd");
                Uri uri = Uri.parse(getVideoPlayPath());
                mPlayerManager.setPlayUri(uri);
                mPlayerManager.play();
            }

            @Override
            public void onStartPlay() {
                Log.d(TAG, "onStatPlay");
            }

            @Override
            public void onError(Exception error) {
                Log.d(TAG, "onError:" + error.getMessage());
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
        mPlayerManager.setSurface(mSurface);
        mPlayerManager.play();
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
        mPlayerManager.release();
    }
}
