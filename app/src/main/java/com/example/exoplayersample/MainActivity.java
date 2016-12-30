package com.example.exoplayersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;


import com.example.exoplayersample.video.listdemo.adapter.VideoListAdapter;
import com.example.exoplayersample.video.listdemo.bean.VideoInfo;
import com.example.exoplayersample.video.listdemo.listener.PlayWindowScrollerListener;
import com.example.exoplayersample.video.listdemo.manager.DefaultVideoPlayManager;
import com.example.exoplayersample.video.listdemo.manager.VideoPlayManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private VideoListAdapter mAdapter;
    private VideoPlayManager mVideoPlayManager;

    private PlayWindowScrollerListener mScrollerListener;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        ArrayList<VideoInfo> videoInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoInfo info = new VideoInfo();
            info.setDesc("des");
            //http://clips.vorwaerts-gmbh.de/VfE_html5.mp4
            info.setVideoUrl("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4");
            info.setThumbnailUrl("http://img2.imgtn.bdimg.com/it/u=11396313,1297606499&fm=21&gp=0.jpg");
            videoInfos.add(info);
        }
        //
        mVideoPlayManager = new DefaultVideoPlayManager();

        mAdapter = new VideoListAdapter();
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(videoInfos);
        mAdapter.setVideoPlayManager(mVideoPlayManager);
        mScrollerListener = new PlayWindowScrollerListener(mVideoPlayManager);
        mRecyclerView.addOnScrollListener(mScrollerListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mScrollerListener.onScrollStateChanged(mRecyclerView, RecyclerView.SCROLL_STATE_IDLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.release();
        }
    }
}
