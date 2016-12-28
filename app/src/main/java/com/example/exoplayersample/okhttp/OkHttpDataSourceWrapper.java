package com.example.exoplayersample.okhttp;

import android.net.Uri;

import com.example.exoplayersample.bean.CacheGlue;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import java.util.List;
import java.util.Map;

/**
 * Created by 龙泉 on 2016/12/27.
 */

public class OkHttpDataSourceWrapper implements HttpDataSource.Factory {

    private OkHttpDataSourceFactory mDataSource;
    private CacheGlue mGlue;

    public OkHttpDataSourceWrapper(CacheGlue glue, OkHttpDataSourceFactory dataSource) {
        this.mDataSource = dataSource;
        this.mGlue = glue;
    }

    @Override
    public HttpDataSource createDataSource() {
        return new OkhttpDataSourceWrapper(mDataSource.createDataSource());
    }

    private class OkhttpDataSourceWrapper implements HttpDataSource {

        private OkHttpDataSource mOkHttpDataSource;

        public OkhttpDataSourceWrapper(OkHttpDataSource dataSource) {
            this.mOkHttpDataSource = dataSource;
        }

        @Override
        public long open(DataSpec dataSpec) throws HttpDataSourceException {
            return mOkHttpDataSource.open(dataSpec);
        }

        @Override
        public void close() throws HttpDataSourceException {
            mOkHttpDataSource.close();
            if (mGlue != null) {
                mGlue.glueAll();
            }
        }

        @Override
        public int read(byte[] buffer, int offset, int readLength) throws HttpDataSourceException {
            return mOkHttpDataSource.read(buffer, offset, readLength);
        }

        @Override
        public Uri getUri() {
            return mOkHttpDataSource.getUri();
        }

        @Override
        public void setRequestProperty(String name, String value) {
            mOkHttpDataSource.setRequestProperty(name, value);
        }

        @Override
        public void clearRequestProperty(String name) {
            mOkHttpDataSource.clearRequestProperty(name);
        }

        @Override
        public void clearAllRequestProperties() {
            mOkHttpDataSource.clearAllRequestProperties();
        }

        @Override
        public Map<String, List<String>> getResponseHeaders() {
            return mOkHttpDataSource.getResponseHeaders();
        }
    }
}
