package com.rest.ybp.playlist;

import com.rest.ybp.audio.Audio;
import com.rest.ybp.user.User;
import jakarta.persistence.*;

@Entity
public class Playlist {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PLAYLIST_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "AUDIO_ID")
    private Audio audio;

    public Playlist(User user, Audio audio) {
        this.user = user;
        this.audio = audio;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Audio getAudio() {
        return audio;
    }
}
