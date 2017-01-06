package com.younchen.myexoplayer.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.younchen.myexoplayer.MyExoPlayerEnv;
import com.younchen.myexoplayer.okhttp.CacheOkHttpDataSourceFactory;
import com.younchen.myexoplayer.player.listener.PlayerListener;
import com.younchen.myexoplayer.util.FileUtils;
import com.younchen.myexoplayer.util.UriUtils;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by 龙泉 on 2016/12/26.
 */
public class DefaultPlayer implements Player, ExoPlayer.EventListener {

    private SimpleExoPlayer simpleExoPlayer;
    private String mUserAgent;
    private Handler mainHandler;

    private Uri mUri;
    private Surface mSurface;
    private PlayerListener mListener;

    private static final String TAG = "DefaultPlayer";
    private static final String USER_AGENT = "DefaultPlayer";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    public  DefaultPlayer(){
        mainHandler = new Handler();
        simpleExoPlayer = getPlayerInstance();
        mUserAgent = Util.getUserAgent(MyExoPlayerEnv.getContext(), "ExoPlayerDemo");
    }

    @Override
    public void setSurface(Surface surface) {
        this.mSurface = surface;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setVideoSurface(mSurface);
        }
    }

    @Override
    public void play() {
        prepare();
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public Player setPlayUri(Uri uri) {
        this.mUri = uri;
        return this;
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

    @Override
    public long getDuration() {
        return simpleExoPlayer == null || simpleExoPlayer.getDuration() < 0 ? 0 : simpleExoPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return simpleExoPlayer.getCurrentPosition();
    }

    @Override
    public long getBufferedPercentage() {
        return simpleExoPlayer.getBufferedPercentage();
    }

    @Override
    public boolean isPlaying() {
        return simpleExoPlayer != null && simpleExoPlayer.getPlayWhenReady();
    }

    @Override
    public void pause() {
        if (simpleExoPlayer != null && simpleExoPlayer.getPlayWhenReady()) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void seekTo(long position) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.seekTo(position);
        }
    }

    @Override
    public void resume() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void stop() {
        if(simpleExoPlayer != null){
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
        boolean useOkHttp;
        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme) && scheme.contains("http")) {
            useOkHttp = true;
        } else {
            useOkHttp = false;
        }
        if(useOkHttp){
            return new ExtractorMediaSource(uri, getDataSourceFactory(uri), new DefaultExtractorsFactory(),
                    mainHandler, null);
        }else{
            return buildMediaSource(MyExoPlayerEnv.getContext(), uri);
        }
    }

    private DataSource.Factory getDataSourceFactory(Uri uri) {
        String path = FileUtils.convertUrlToLocalPath(uri.getPath());
        File cacheFile = new File(path);
        return new CacheOkHttpDataSourceFactory(new OkHttpClient(), mUserAgent, new DefaultBandwidthMeter(), cacheFile);
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

    private MediaSource buildMediaSource(Context context, Uri uri) {
        int type = UriUtils.inferContentType(uri.toString());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, new DefaultDataSourceFactory(context, null,
                        new DefaultHttpDataSourceFactory(USER_AGENT, null)),
                        new DefaultSsChunkSource.Factory(new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                                new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER))), mainHandler, null);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, new DefaultDataSourceFactory(context, null,
                        new DefaultHttpDataSourceFactory(USER_AGENT, null)),
                        new DefaultDashChunkSource.Factory(new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                                new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER))), mainHandler, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER)), mainHandler, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, new DefaultDataSourceFactory(context, BANDWIDTH_METER,
                        new DefaultHttpDataSourceFactory(USER_AGENT, BANDWIDTH_METER)), new DefaultExtractorsFactory(),
                        mainHandler, null);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.d(TAG, "onTimeLineChanged:" +  timeline.toString());
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d(TAG, "onTracksChanged track group:" +  trackGroups.toString() + " trackSelection:" + trackSelections);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d(TAG, "onLoadingChanged:" + isLoading);
        if (mListener != null) {
            mListener.onLoading(isLoading);
        }
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
                if (mListener != null) {
                    mListener.onPlayEnd();
                }
                break;
            case ExoPlayer.STATE_IDLE:
                text += "idle";
                break;
            case ExoPlayer.STATE_READY:
                text += "ready";
                if (mListener != null ) {
                    if(playWhenReady){
                        mListener.onStartPlay();
                    }else{
                        mListener.onPausePlay();
                    }
                }
                if(mListener != null && !playWhenReady){
                    mListener.onPausePlay();
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
