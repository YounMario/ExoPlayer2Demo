package com.example.exoplayersample.video.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoInfo implements Parcelable {


    private long id;
    private String videoUrl;
    private String thumbnailUrl;
    private String title;
    private String desc;
    private String videoLocalPath;

    public VideoInfo() {
    }

    private VideoInfo(Parcel in) {
        this.id = in.readLong();
        this.videoUrl = in.readString();
        this.thumbnailUrl = in.readString();
        this.title = in.readString();
        this.desc = in.readString();
        this.videoLocalPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(videoLocalPath);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoLocalPath() {
        return videoLocalPath;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + id +
                ", videoDownloadUrl = " + videoUrl +
                ", thumbnailUrl =" + thumbnailUrl +
                ", title = " + title +
                ", desc = " + desc +
                ", videoLocalPath='" + videoLocalPath +
                '}';
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel in) {
            return new VideoInfo(in);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
