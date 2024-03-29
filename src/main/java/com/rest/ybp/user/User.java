package com.rest.ybp.user;

import com.rest.ybp.playlist.Playlist;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private int id;

    @Column(name = "USER_NAME", unique = true)
    private String name;

    @Column(name = "USER_password")
    private String password;

    @Column(name = "USER_EMAIL", unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Playlist> playLists;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Playlist> getPlayLists() {
        return playLists;
    }
}
