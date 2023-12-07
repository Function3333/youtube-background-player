package com.rest.ybp.audio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class Audio {
    @Id
    @Column(name = "AUDIO_ID")
    private String id;

    @Column(name = "AUDIO_TITLE")
    private String title;

    public Audio(String id, String title) {
        this.id = id;
        this.title = title;
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
