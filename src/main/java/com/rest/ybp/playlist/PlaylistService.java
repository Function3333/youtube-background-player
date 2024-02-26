package com.rest.ybp.playlist;

import com.rest.ybp.s3.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private BucketRepository bucketRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, BucketRepository bucketRepository) {
        this.playlistRepository = playlistRepository;
        this.bucketRepository = bucketRepository;
    }

    @Transactional
    public int savePlaylist(Playlist playlist) {
        return playlistRepository.savePlaylist(playlist);
    }

    @Transactional
    public void deletePlaylist(Playlist playlist) {
        int count = playlistRepository.countByAudioId(playlist.getAudio().getId());

        if(count == 0) bucketRepository.deleteAudio(playlist.getAudio().getYoutubeTitle());
        playlistRepository.deletePlaylist(playlist);
    }

}
