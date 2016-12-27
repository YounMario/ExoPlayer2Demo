package com.example.exoplayersample.bean;

import android.net.Uri;
import android.util.Log;

import com.example.exoplayersample.App;
import com.example.exoplayersample.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class CacheGlue {
    private static final String sPath = App.getInstance().getFilesDir().getAbsolutePath();
    private static final String TAG = "CacheGlue";
    private Uri uri;
    private List<RangeFile> rangeFiles;
    private long length;
    private String md5;
    private String path;
    private BufferedSink completeSink;

    public CacheGlue(Uri uri) {
        this.uri = uri;

        String url = uri.getPath();
        md5 = url.substring(url.lastIndexOf('/') + 1, url.length());
        rangeFiles = new ArrayList<>();
        path = sPath + "/complete_" + md5;
        try {
            completeSink = Okio.buffer(Okio.sink(new File(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getMd5() {
        return md5;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean glueAll() {
        Log.i(TAG, "glueAll, length: " + length + ", rangeFiles' size: " + rangeFiles.size());
        Collections.sort(rangeFiles, new Comparator<RangeFile>() {
            @Override
            public int compare(RangeFile rangeFile, RangeFile t1) {
                if (rangeFile.start > t1.start) {
                    return 1;
                } else if (rangeFile.start == t1.start) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        if (length == 0) {
            return false;
        } else {
            long size = 0;
            for (RangeFile rangeFile : rangeFiles) {
                if (size >= rangeFile.start) {
                    size = rangeFile.end;// rangeFile.start + rangeFile.fileSink.buffer().size();
                    Log.i(TAG, "add glue file size: " + size);
                } else {
                    Log.i(TAG, "not enough file to glue whole file.");
                    return false;
                }
            }
            if (size != length) {
                return false;
            }
            Log.i(TAG, "can glue all, length: " + length);
        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long size = 0;
                try {
                    for (RangeFile rangeFile : rangeFiles) {
                        if (rangeFile.file == null || !rangeFile.file.exists()) {
                            continue;
                        }
                        BufferedSource source = Okio.buffer(Okio.source(rangeFile.file));
                        if (size > rangeFile.start) {
                            source.skip(size - rangeFile.start);
                        }
                        size += source.readAll(completeSink);
                        Log.i(TAG, "completeSink size: " + size);
                    }
                    completeSink.close();
                    FileUtils.moveCompleteFilmToFinalPath(path, md5);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.i(TAG, "opps:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return false;
    }

    public RangeFile addCache(String md5, long range) {
        Log.i(TAG, "addCache, range: " + range);
        RangeFile file = new RangeFile(md5, range);
        rangeFiles.add(file);
        return file;
    }
}
