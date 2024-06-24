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

import java.util.HashMap;

import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/audio")
    public Response postAudio(@RequestBody HashMap<String, String> jsonMap, @RequestHeader("ACCESS_TOKEN") String accessToken) {
        Result result = null;
        
        try {
            String Id = jsonMap.get("videoId");
            String title = jsonMap.get("title");
            String thumbnailUrl = jsonMap.get("thumbnailUrl");
            String length = jsonMap.get("length");
            
            String userName = jwtManager.parseToken(accessToken);
            if(true) {
                User user = userService.getUserByName(userName);
                Youtube youtube = new Youtube(Id, title, thumbnailUrl, length);
                Audio savedAudio = audioService.postAudio(youtube);

                if(savedAudio != null && user != null) {
                    Playlist playlist = new Playlist(user, savedAudio);
                    playlistService.savePlaylist(playlist);
    
                    result = Result.SUCCESS;
                } else {
                    result = Result.POST_AUDIO_FAIL;
                }
            }
        //catch문 Excpetion 업데이트하기
        } catch (Exception e) {
            System.out.println("[AudioController] postAudio Failed");
            result = Result.POST_AUDIO_FAIL;
            e.printStackTrace();
        }
        
        result = (result == null) ? Result.POST_AUDIO_FAIL : result;
        return new Response(result.getStatus(), result.getMsg());
    }
}
