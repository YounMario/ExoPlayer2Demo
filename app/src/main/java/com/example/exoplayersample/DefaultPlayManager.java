package com.example.exoplayersample;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import com.example.exoplayersample.okhttp.CacheHttpClient;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
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
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by 龙泉 on 2016/12/26.
 */
public class DefaultPlayManager implements PlayerManager, ExoPlayer.EventListener {

    private SimpleExoPlayer simpleExoPlayer;
    private String mUserAgent;
    private Handler mainHandler;

    private Uri mUri;
    private Surface mSurface;

    @Override
    public void init() {
        mainHandler = new Handler();
        simpleExoPlayer = getPlayerInstance();
        mUserAgent = Util.getUserAgent(App.getInstance(), "ExoPlayerDemo");
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
        return new ExtractorMediaSource(uri, getDataSourceFactory(true), new DefaultExtractorsFactory(),
                mainHandler, null);
    }

    private HttpDataSource.Factory getDataSourceFactory(boolean useOkhttp) {
        if (useOkhttp) {
            return new OkHttpDataSourceFactory(CacheHttpClient.getInstance(), mUserAgent, new DefaultBandwidthMeter());
        } else {
            return new DefaultHttpDataSourceFactory(mUserAgent, new DefaultBandwidthMeter());
        }
    }


    private SimpleExoPlayer getPlayerInstance() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        return ExoPlayerFactory.newSimpleInstance(App.getInstance(), trackSelector, loadControl);
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
                break;
            case ExoPlayer.STATE_ENDED:
                text += "ended";
                simpleExoPlayer.seekTo(0);
                break;
            case ExoPlayer.STATE_IDLE:
                text += "idle";
                break;
            case ExoPlayer.STATE_READY:
                text += "ready";
                break;
            default:
                text += "unknown";
                break;
        }
        Log.e("exoPlayer", text);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
