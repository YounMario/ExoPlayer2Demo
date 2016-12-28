package com.example.exoplayersample.video.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.bean.VideoInfo;
import com.example.exoplayersample.video.holder.VideoItemHolder;
import com.example.exoplayersample.video.player.PlayableWindow;
import com.example.exoplayersample.video.player.manager.VideoPlayManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 龙泉 on 2016/12/15.
 */

public class VideoListAdapter extends RecyclerView.Adapter {


    private static final String TAG = "VideoListAdapter";

    private static final String TAG_ITEM_STATE = "video_item_state";
    private static final String TAG_SAVE_CURRENT = "save_current_seek";
    private int currentState;

    private PlayableWindow currentWindow;
    private RecyclerView mRecycleView;


    private List<VideoInfo> data;

    private LinearLayoutManager mLinearLayoutManager;

    private static final int STATE_INIT = -1;
    private static final int STATE_INITED = 0;

    private VideoPlayManager mVideoPlayManager;


    public void release() {
        Log.i(TAG_ITEM_STATE, "release player:" + this);
        mVideoPlayManager.release();
    }

    public void setData(List<VideoInfo> videos) {
        data = videos;
        notifyDataSetChanged();
    }


    public void setLinearLayout(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }


    public VideoListAdapter(RecyclerView recyclerView) {
        mRecycleView = recyclerView;
        setCurrentState(STATE_INIT);

        data = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        VideoItemHolder videoItemHolder = new VideoItemHolder(itemView);
        videoItemHolder.setVideoPlayManager(mVideoPlayManager);
        return videoItemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VideoInfo videoInfo = getItem(position);
        final VideoItemHolder window = (VideoItemHolder) holder;
        window.updateVideoItem(videoInfo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public VideoInfo getItem(int position) {
        return data.get(position);
    }


    private void setCurrentState(int currentState) {
        Log.i("aStateChanged", " currentState:" + currentState);
        this.currentState = currentState;
    }

    public void setVideoPlayManager(VideoPlayManager videoPlayManager){
        this.mVideoPlayManager = videoPlayManager;
    }

}
