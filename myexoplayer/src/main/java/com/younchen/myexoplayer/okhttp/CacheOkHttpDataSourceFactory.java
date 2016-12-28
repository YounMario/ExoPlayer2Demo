package com.younchen.myexoplayer.okhttp;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

import okhttp3.CacheControl;
import okhttp3.Call;

/**
 * Created by 龙泉 on 2016/12/28.
 */

public class CacheOkHttpDataSourceFactory implements DataSource.Factory{

    private final Call.Factory callFactory;
    private final String userAgent;
    private final TransferListener<? super DataSource> listener;
    private final CacheControl cacheControl;

    private File outPutFile;

    /**
     * @param callFactory A {@link Call.Factory} (typically an {@link okhttp3.OkHttpClient}) for use
     *     by the sources created by the factory.
     * @param userAgent The User-Agent string that should be used.
     * @param listener An optional listener.
     */
    public CacheOkHttpDataSourceFactory(Call.Factory callFactory, String userAgent,
                                   TransferListener<? super DataSource> listener, File outPutFile) {
        this(callFactory, userAgent, listener, null,outPutFile);
    }

    /**
     * @param callFactory A {@link Call.Factory} (typically an {@link okhttp3.OkHttpClient}) for use
     *     by the sources created by the factory.
     * @param userAgent The User-Agent string that should be used.
     * @param listener An optional listener.
     * @param cacheControl An optional {@link CacheControl} for setting the Cache-Control header.
     */
    public CacheOkHttpDataSourceFactory(Call.Factory callFactory, String userAgent,
                                   TransferListener<? super DataSource> listener, CacheControl cacheControl, File outPutFile) {
        this.callFactory = callFactory;
        this.userAgent = userAgent;
        this.listener = listener;
        this.cacheControl = cacheControl;
        this.outPutFile = outPutFile;
    }

    @Override
    public DataSource createDataSource() {
        return new CacheDataSource(new SimpleCache(outPutFile, new LeastRecentlyUsedCacheEvictor(Integer.MAX_VALUE)),
                new OkHttpDataSource(callFactory, userAgent, null, listener, cacheControl),
                CacheDataSource.FLAG_BLOCK_ON_CACHE, Integer.MAX_VALUE);
    }
}
