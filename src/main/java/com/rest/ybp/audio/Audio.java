package com.rest.ybp.audio;

import com.rest.ybp.playlist.Playlist;
import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany(mappedBy = "audio")
    private List<Playlist> playList;

    public Audio() {
    }

    public Audio(String youtubeId, String youtubeTitle, String audioUrl) {
        this.youtubeId = youtubeId;
        this.youtubeTitle = youtubeTitle;
        this.audioUrl = audioUrl;
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

    public List<Playlist> getPlayList() {
        return playList;
    }
}
