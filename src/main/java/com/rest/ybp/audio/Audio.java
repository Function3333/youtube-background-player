package com.rest.ybp.audio;

import jakarta.persistence.*;


@Entity
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUDIO_ID")
    private int id;

    @Column(name = "AUDIO_YOUTUBE_ID", unique = true)
    private String youtubeId;

    @Column(name = "AUDIO_TITLE")
    private String youtubeTitle;

    @Column(name = "AUDIO_CHANNEL_TITLE")
    private String youtubeChannelTitle;

    @Column(name = "AUDIO_URL")
    private String audioUrl;

    @Column(name = "AUDIO_THUMBNAIL_URL")
    private String thumbnailUrl;

    @Column(name = "AUDIO_LENGTH")
    private String length;

    // @OneToMany(mappedBy = "audio")
    // private List<Playlist> playList;

    public Audio() {
    }

    public Audio(String youtubeId, String youtubeTitle, String youtubeChannelTitle, String audioUrl, String thumbnailUrl, String length) {
        this.youtubeId = youtubeId;
        this.youtubeTitle = youtubeTitle;
        this.youtubeChannelTitle = youtubeChannelTitle;
        this.audioUrl = audioUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public String getYoutubeTitle() {
        return youtubeTitle;
    }

    public String getYoutubeChannelTitle() {
        return youtubeChannelTitle;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

     public String getThumbnailUrl() {
         return thumbnailUrl;
     }

     public void setThumbnailUrl(String thumbnailUrl) {
         this.thumbnailUrl = thumbnailUrl;
     }

     public String getLength() {
         return length;
     }
     
     public void setLength(String length) {
         this.length = length;
     }
}
