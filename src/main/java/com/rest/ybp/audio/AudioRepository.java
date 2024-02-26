package com.rest.ybp.audio;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AudioRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public AudioRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Audio getById(int audioId) {
        QAudio qAudio = QAudio.audio;

        return queryFactory
                .selectFrom(qAudio)
                .where(qAudio.id.eq(audioId))
                .fetchOne();
    }

    public Audio getByYoutubeId(String youtubeId) {
        QAudio qAudio = QAudio.audio;

        return queryFactory
                .selectFrom(qAudio)
                .where(qAudio.youtubeId.eq(youtubeId))
                .fetchOne();
    }



    public int save(Audio audio) {
        em.persist(audio);
        return audio.getId();
    }
}
