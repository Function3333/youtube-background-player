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

    public Audio(String youtubeId, String youtubeTitle) {
        this.youtubeId = youtubeId;
        this.youtubeTitle = youtubeTitle;
    }

    public int getId() {
        return id;
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
}
