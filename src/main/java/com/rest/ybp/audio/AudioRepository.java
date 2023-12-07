package com.rest.ybp.audio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;

@Repository
public class AudioRepository {
    private EntityManager em;

    @Autowired
    public AudioRepository(EntityManager em) {
        this.em = em;
    }

    public String save(Audio audio) {
        em.persist(audio);

        return audio.getId();
    }
}
