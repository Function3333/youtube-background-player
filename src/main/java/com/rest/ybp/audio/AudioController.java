package com.rest.ybp.audio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.playlist.Playlist;
import com.rest.ybp.playlist.PlaylistService;

import com.rest.ybp.user.User;
import com.rest.ybp.user.UserService;
import com.rest.ybp.utils.JwtUtil;
import com.rest.ybp.youtube.Youtube;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AudioController {
    private final AudioService audioService;
    private final UserService userService;
    private final PlaylistService playlistService;
    private final JwtUtil jwtManager;

    public AudioController(AudioService audioService, UserService userService, PlaylistService playlistService, JwtUtil jwtManager) {
        this.audioService = audioService;
        this.userService = userService;
        this.playlistService = playlistService;
        this.jwtManager = jwtManager;
    }

    @GetMapping("/audio")
    public Response getAudio(@RequestParam("id") String audioId){
        Audio findById = audioService.getAudio(audioId);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return new Response(Result.SUCCESS.getStatus(), mapper.writeValueAsString(findById));
        } catch (NullPointerException | JsonProcessingException e) {
            return new Response(Result.GET_AUDIO_FAIL.getStatus(), Result.GET_AUDIO_FAIL.getMsg());
        }
    }

    //인터셉터 적용해서 미리 token 검사한 후 로직 실행하게 변경
    @PostMapping("/audio")
    public Response postAudio(@RequestParam("videoId") String videoId
                                ,@RequestParam("title") String title
                                ,@RequestParam("thumbnailUrl") String thumbnailUrl
                                ,@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        Youtube youtube = new Youtube(videoId, title, thumbnailUrl);
        Result result = audioService.postAudio(youtube);

        // if(result == Result.SUCCESS) {
        //     String userName = jwtManager.parseAccessToken(accessToken);
        //     User user = userService.getUserByName(userName);

        //     if(user != null) {
        //         List<String> youtubeIdList = audioService.getAudioByFullUrl(createFullUrl(url, listId));

        //         for(String youtubeId : youtubeIdList) {
        //             Audio audio = audioService.getByYoutubeId(youtubeId);

        //             Playlist playlist = new Playlist(user, audio);
        //             playlistService.savePlaylist(playlist);
        //         }
        //     }
        // }

        return new Response(result.getStatus(), result.getMsg());
    }

    public String createFullUrl(String youtubeId, String listId) {
        return listId == null ? youtubeId : youtubeId + "&list=" + listId;
    }
}
