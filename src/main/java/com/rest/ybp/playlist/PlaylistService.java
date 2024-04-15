package com.rest.ybp.playlist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rest.ybp.utils.S3Util;

@Service
@Transactional(readOnly = true)
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private S3Util bucketRepository;

    public PlaylistService(PlaylistRepository playlistRepository, S3Util bucketRepository) {
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
