package com.younchen.myexoplayer.player;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.younchen.myexoplayer.MyExoPlayerEnv;
import com.younchen.myexoplayer.okhttp.CacheOkHttpDataSourceFactory;
import com.younchen.myexoplayer.player.listener.PlayerListener;
import com.younchen.myexoplayer.util.FileUtils;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by 龙泉 on 2016/12/26.
 */
public class DefaultPlayManager implements PlayerManager, ExoPlayer.EventListener {

    private SimpleExoPlayer simpleExoPlayer;
    private String mUserAgent;
    private Handler mainHandler;

    private Uri mUri;
    private Surface mSurface;
    private PlayerListener mListener;

    @Override
    public void init() {
        mainHandler = new Handler();
        simpleExoPlayer = getPlayerInstance();
        mUserAgent = Util.getUserAgent(MyExoPlayerEnv.getContext(), "ExoPlayerDemo");
    }

    @Override
    public void setSurface(Surface surface) {
        this.mSurface = surface;
    }

    @Override
    public void play() {
        prepare();
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void setPlayUri(Uri uri) {
        this.mUri = uri;
    }

    @Override
    public void release() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
    }

    @Override
    public void setPlayerListener(PlayerListener listener) {
        this.mListener = listener;
    }

    private void prepare() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
        simpleExoPlayer = getPlayerInstance();
        simpleExoPlayer.addListener(this);
        simpleExoPlayer.setVideoSurface(mSurface);
        simpleExoPlayer.prepare(buildMediaSource(mUri));
    }

    private MediaSource buildMediaSource(Uri uri) {
        boolean useOkHttp;
        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme) && scheme.contains("http")) {
            useOkHttp = true;
        } else {
            useOkHttp = false;
        }
        return new ExtractorMediaSource(uri, getDataSourceFactory(useOkHttp, uri), new DefaultExtractorsFactory(),
                mainHandler, null);
    }

    private DataSource.Factory getDataSourceFactory(boolean useOkhttp, Uri uri) {
        if (useOkhttp) {
            String path = FileUtils.convertUrlToLocalPath(uri.getPath());
            File cacheFile = new File(path);
            return new CacheOkHttpDataSourceFactory(new OkHttpClient(), mUserAgent, new DefaultBandwidthMeter(), cacheFile);
        } else {
            return new DefaultDataSourceFactory(MyExoPlayerEnv.getContext(), mUserAgent);
        }
    }


    private SimpleExoPlayer getPlayerInstance() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        return ExoPlayerFactory.newSimpleInstance(MyExoPlayerEnv.getContext(), trackSelector, loadControl);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                text += "buffering";
                if (mListener != null) {
                    mListener.onBuffering();
                }
                break;
            case ExoPlayer.STATE_ENDED:
                text += "ended";
                if (mListener != null) {
                    mListener.onPlayEnd();
                }
                break;
            case ExoPlayer.STATE_IDLE:
                text += "idle";
                break;
            case ExoPlayer.STATE_READY:
                text += "ready";
                if (mListener != null && playWhenReady) {
                    mListener.onStartPlay();
                }
                break;
            default:
                text += "unknown";
                break;
        }
        Log.e("exoPlayer", text);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (mListener != null) {
            mListener.onError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
