package com.rest.ybp.audio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.ybp.common.Response;
import com.rest.ybp.common.Result;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AudioController {
    private final AudioService audioService;

    @Autowired
    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @GetMapping("/audio")
    public Response getAudio(@RequestParam("id") String audioId){
        Audio findById = audioService.getAudio(audioId);
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Result.SUCCESS.getStatus() = " + Result.SUCCESS.getStatus());
            return new Response(Result.SUCCESS.getStatus(), mapper.writeValueAsString(findById));
        } catch (NullPointerException | JsonProcessingException e) {
            return new Response(Result.GET_AUDIO_FAIL.getStatus(), Result.GET_AUDIO_FAIL.getMsg());
        }
    }

    @PostMapping("/audio")
    public Response postAudio(@RequestParam("url")String url, @RequestParam(value = "list", required = false)String listId) {
        String fullUrl = createFullUrl(url, listId);
        Result result = audioService.postAudio(fullUrl);

        return new Response(result.getStatus(), result.getMsg());
    }

    public String createFullUrl(String youtubeId, String listId) {
        return listId == null ? youtubeId : youtubeId + "&list=" + listId;
    }
}
