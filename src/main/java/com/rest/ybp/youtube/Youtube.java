package com.rest.ybp.youtube;

public class Youtube {
    private String id;
    private String title;
    private String thumbnailUrl;
    private String length;   
    private String channelTitle;
    
    public Youtube() {}

    public Youtube(String id, String title, String channelTitle, String thumbnailUrl, String length) {
        this.id = id;
        this.title = title;
        this.channelTitle = channelTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.length = length;
    }    

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
