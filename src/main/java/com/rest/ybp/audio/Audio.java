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

    @Column(name = "AUDIO_URL")
    private String audioUrl;

    @Column(name = "AUDIO_THUMBNAIL_URL")
    private String thumbnailUrl;

    // @OneToMany(mappedBy = "audio")
    // private List<Playlist> playList;

    public Audio() {
    }

    public Audio(String youtubeId, String youtubeTitle, String audioUrl, String thumbnailUrl) {
        this.youtubeId = youtubeId;
        this.youtubeTitle = youtubeTitle;
        this.audioUrl = audioUrl;
        this.thumbnailUrl = thumbnailUrl;
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

    public String getAudioUrl() {
        return audioUrl;
    }

     public String getThumbnailUrl() {
         return thumbnailUrl;
     }

     public void setThumbnailUrl(String thumbnailUrl) {
         this.thumbnailUrl = thumbnailUrl;
     }
}
