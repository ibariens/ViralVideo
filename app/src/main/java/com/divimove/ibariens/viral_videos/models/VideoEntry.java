package com.divimove.ibariens.viral_videos.models;


public final class VideoEntry {
    public final String title;
    public final String videoId;
    public final Boolean watched;

    public VideoEntry(String title, String videoId, Boolean watched) {
        this.title   = title;
        this.videoId = videoId;
        this.watched = watched;
    }
}