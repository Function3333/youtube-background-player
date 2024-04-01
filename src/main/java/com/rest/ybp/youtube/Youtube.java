package com.rest.ybp.youtube;

public class Youtube {
    private String videoId;
    private String videoTitle;
    private String thumbnailUrl;

    public Youtube(String videoId, String videoTitle, String thumnailurl) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.thumbnailUrl = thumnailurl; 
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return this.videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
