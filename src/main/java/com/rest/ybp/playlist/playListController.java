package com.rest.ybp.playlist;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import com.rest.ybp.utils.JwtUtil;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class playListController {
    private final PlaylistService playlistService;
    private final JwtUtil jwtUtil;

    public playListController(PlaylistService playlistService, JwtUtil jwtUtil) {
        this.playlistService = playlistService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/playList")
    public Response getPlayList(@RequestHeader("ACCESS_TOKEN") String accessToken) {
        Result result = Result.GET_PLAYLIST_FAIL;
        String jsonPlayList = null;

        try {
            String userName = jwtUtil.parseToken(accessToken);
            if(userName != null) {
                List<Playlist> userPlayList = playlistService.getUserPlayList(userName);
                result = Result.SUCCESS;

                jsonPlayList = new ObjectMapper().writeValueAsString(userPlayList);
            }    

            if(userName == null) {
                result = Result.PARSE_TOKEN_FAIL;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result = Result.GET_PLAYLIST_FAIL;
        }

        return new Response(result.getStatus(), jsonPlayList); 
    }

    @DeleteMapping("/playList")
    public Response deletePlayList(@RequestHeader("ACCES_TOKEN")String accessToken, @RequestParam("audio_id") String audioId) {
        Result result = Result.DELETE_PLAYLIST_FAIL;
        String userName = jwtUtil.parseToken(accessToken);
        
        if(userName != null && audioId != null) {
            try {
                playlistService.deleteUserPlayList(userName, Integer.parseInt(userName));    
                result = Result.SUCCESS;
            } catch (NumberFormatException e) {
                result = Result.DELETE_PLAYLIST_FAIL;
            }
            
        }
        return new Response(result.getStatus(), result.getMsg());
    }
    
    
}
