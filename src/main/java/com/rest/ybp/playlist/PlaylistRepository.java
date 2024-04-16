package com.rest.ybp.playlist;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * fetch()는 조건에 해당하는 데이터가 없으면 null이 아닌 빈 배열 반환
 */
@Repository
public class PlaylistRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PlaylistRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public int savePlaylist(Playlist playlist) {
        String userName = playlist.getUser().getName();
        int audioId = playlist.getAudio().getId();

        Playlist userPlaylist = getPlayListByUserIdAndAudioId(userName, audioId);

        if(userPlaylist == null) {
            em.persist(playlist);
        }
        return playlist.getId();
    }

    public void deletePlaylist(Playlist playlist) {
        em.remove(playlist);
    }

    public List<Playlist> getByUserName(String userName) {
        QPlaylist qPlaylist = QPlaylist.playlist;

        return queryFactory
                .selectFrom(qPlaylist)
                .where(qPlaylist.user.name.eq(userName))
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

    public Playlist getPlayListByUserIdAndAudioId(String userName, int audioId) {
        QPlaylist qPlaylist = QPlaylist.playlist;

        return queryFactory
                .selectFrom(qPlaylist)
                .where(qPlaylist.user.name.eq(userName)
                .and(qPlaylist.audio.id.eq(audioId)))
                .fetchFirst();    
    }
}
