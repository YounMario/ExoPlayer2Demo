package com.younchen.myexoplayer.okhttp;

import android.text.TextUtils;
import android.util.Log;


import com.younchen.myexoplayer.bean.CacheGlue;
import com.younchen.myexoplayer.bean.RangeFile;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by 龙泉 on 2016/12/27.
 * 这个边下载边播放的逻辑， 原理是每次获取的流写入文件，最后将文件合并
 * ExoPlayer中 已经有CacheDataSource 可以实现这个功能
 */
@Deprecated
public class CacheHttpClient{

    private CacheGlue mGlue;

    public CacheHttpClient(CacheGlue glue){
        mGlue = glue;
    }

    public OkHttpClient getInstance() {

        return new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Response originalResponse = chain.proceed(chain.request());

                String md5 = mGlue.getMd5();//chain.request().httpUrl().;
                String range = chain.request().headers().get("range");
                if (!TextUtils.isEmpty(range) && range.startsWith("bytes=")) {
                    range = range.substring(6);
                    range = range.substring(0, range.indexOf("-"));
                } else {
                    range = "0";
                    mGlue.setLength(originalResponse.body().contentLength());
                }
                RangeFile rangeFile = mGlue.addCache(md5, Long.valueOf(range));
                return originalResponse.newBuilder()
                        .body(new DeepCopyResponseBody(originalResponse.body(), rangeFile))
                        .build();
            }
        }).build();
    }


    private static class DeepCopyResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private BufferedSource bufferedSource;
        private BufferedSink fileSink;
        private RangeFile rangefile;

        public DeepCopyResponseBody(ResponseBody responseBody, RangeFile rangeFile) {
            this.responseBody = responseBody;
            this.rangefile = rangeFile;
            this.fileSink = rangeFile.fileSink;
        }

        public void closeFileSink() {
            try {
                Log.d("DeepCopyResponseBody", "filesink size: " + fileSink.buffer().size());
                fileSink.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            Log.d("DeepCopyResponseBody", "length :" + responseBody.contentLength());
            Log.d("DeepCopyResponseBody", "type :" + responseBody.contentType());
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    Buffer clone = sink.clone();
                    long size = fileSink.writeAll(clone);
                    rangefile.end += size;
                    return bytesRead;
                }
            };
        }
    }

}
