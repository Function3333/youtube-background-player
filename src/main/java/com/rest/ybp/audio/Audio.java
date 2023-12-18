package com.rest.ybp.audio;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUDIO_ID")
    private int id;

    @Column(name = "AUDIO_YOUTUBE_ID")
    private String youtubeId;

    @Column(name = "AUDIO_TITLE")
    private String youtubeTitle;

    @Column(name = "AUDIO_URL")
    private String audioUrl;

    public Audio(String youtubeId, String youtubeTitle, String audioUrl) {
        this.youtubeId = youtubeId;
        this.youtubeTitle = youtubeTitle;
        this.audioUrl = audioUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getYoutubeTitle() {
        return youtubeTitle;
    }

    public void setYoutubeTitle(String youtubeTitle) {
        this.youtubeTitle = youtubeTitle;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
