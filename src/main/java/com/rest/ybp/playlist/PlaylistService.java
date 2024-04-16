package com.rest.ybp.playlist;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rest.ybp.user.UserRepository;
import com.rest.ybp.utils.S3Util;

@Service
@Transactional(readOnly = true)
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private S3Util bucketRepository;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, S3Util bucketRepository) {
        this.playlistRepository = playlistRepository;
        this.bucketRepository = bucketRepository;
    }

    @Transactional
    public int savePlaylist(Playlist playlist) {
        return playlistRepository.savePlaylist(playlist);
    }

    @Transactional
    public void deleteUserPlayList(String userName, int audioId) {
        Playlist userPlaylist = playlistRepository.getPlayListByUserIdAndAudioId(userName, audioId);

        //모두의 플레이리스트에 해당 audio가 존재하지 않는다면 s3 bucket에서 삭제
        int count = playlistRepository.countByAudioId(userPlaylist.getAudio().getId());
        if(count == 1) bucketRepository.deleteAudio(userPlaylist.getAudio().getYoutubeTitle());
        
        playlistRepository.deletePlaylist(userPlaylist);
    }

    public List<Playlist> getUserPlayList(String userName) {
        return playlistRepository.getByUserName(userName);
    }
}
