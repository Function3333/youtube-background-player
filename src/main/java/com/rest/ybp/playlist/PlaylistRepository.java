package com.rest.ybp.playlist;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaylistRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PlaylistRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public int savePlaylist(Playlist playlist) {
        em.persist(playlist);
        return playlist.getId();
    }

    public void deletePlaylist(Playlist playlist) {
        em.remove(playlist);
    }

    public List<Playlist> getByUserId(int userId) {
        QPlaylist qPlaylist = QPlaylist.playlist;

        return queryFactory
                .selectFrom(qPlaylist)
                .where(qPlaylist.user.id.eq(userId))
                .fetch();
    }

    public int countByAudioId(int audioId) {
        QPlaylist qPlaylist = QPlaylist.playlist;

        List<Playlist> playlists = queryFactory
                .selectFrom(qPlaylist)
                .where(qPlaylist.audio.id.eq(audioId))
                .fetch();

        return playlists.size();
    }

}
