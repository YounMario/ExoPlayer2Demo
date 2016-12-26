package com.example.exoplayersample;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

public class ExoPlayerSample extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private PlayerManager mPlayerManager;

    private Surface mSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_sample);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);
        mPlayerManager = PlayerFactory.newInstance();

        mPlayerManager.init();
        Uri uri = Uri.parse("http://img.locker.cmcm.com/livelock/uservideo/90f1353176bc83dffe2f246eba496c7a");
        mPlayerManager.setPlayUri(uri);
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
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        if (mSurface != null) {
            mSurface.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerManager.release();
    }
}
