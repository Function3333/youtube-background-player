package com.rest.ybp.audio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.playlist.Playlist;
import com.rest.ybp.playlist.PlaylistService;
import com.rest.ybp.s3.BucketRepository;

import com.rest.ybp.user.User;
import com.rest.ybp.user.UserService;
import com.rest.ybp.utils.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AudioController {
    private final AudioService audioService;
    private final UserService userService;
    private final PlaylistService playlistService;
    private final JwtManager jwtManager;

    @Autowired
    public AudioController(AudioService audioService, UserService userService, PlaylistService playlistService, JwtManager jwtManager) {
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

    @PostMapping("/audio")
    public Response postAudio(@RequestParam("url")String url,
                              @RequestParam(value = "list", required = false)String listId,
                              @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

        String fullUrl = createFullUrl(url, listId);
        Result result = audioService.postAudio(fullUrl);

        if(result == Result.SUCCESS) {
            String userName = jwtManager.parseAccessToken(accessToken);
            User user = userService.getUserByName(userName);

            if(user != null) {
                List<String> youtubeIdList = audioService.getAudioByFullUrl(createFullUrl(url, listId));

                for(String youtubeId : youtubeIdList) {
                    Audio audio = audioService.getByYoutubeId(youtubeId);

                    Playlist playlist = new Playlist(user, audio);
                    playlistService.savePlaylist(playlist);
                }
            }
        }

        return new Response(result.getStatus(), result.getMsg());
    }

    public String createFullUrl(String youtubeId, String listId) {
        return listId == null ? youtubeId : youtubeId + "&list=" + listId;
    }
}
