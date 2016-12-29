package com.example.exoplayersample.video.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exoplayersample.R;
import com.example.exoplayersample.video.bean.VideoInfo;
import com.example.exoplayersample.video.holder.VideoItemHolder;
import com.example.exoplayersample.video.player.manager.VideoPlayManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 龙泉 on 2016/12/15.
 */

public class VideoListAdapter extends RecyclerView.Adapter {


    private static final String TAG_ITEM_STATE = "video_item_state";


    private List<VideoInfo> data;


    private VideoPlayManager mVideoPlayManager;


    public void release() {
        Log.i(TAG_ITEM_STATE, "release player:" + this);
        mVideoPlayManager.release();
    }

    public void setData(List<VideoInfo> videos) {
        data = videos;
        notifyDataSetChanged();
    }


    public VideoListAdapter() {
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


    public void setVideoPlayManager(VideoPlayManager videoPlayManager) {
        this.mVideoPlayManager = videoPlayManager;
    }

}
