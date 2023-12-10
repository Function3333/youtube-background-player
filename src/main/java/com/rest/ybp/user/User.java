package com.rest.ybp.user;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private int id;

    @Column(name = "USER_NAME")
    private String name;

    @Column(name = "USER_PWD")
    private String pwd;

    @Column(name = "USER_Email")
    private String email;

    public User(String name, String pwd, String email) {
        this.name = name;
        this.pwd = pwd;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getEmail() {
        return email;
    }
}
