package com.rest.ybp.audio;

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
    public Response test(@RequestParam("url")String url, @RequestParam(value = "list", required = false)String listId) {
        String fullUrl = createFullUrl(url, listId);
        Result result = audioService.getAudio(fullUrl);

        return new Response(result, result.getMsg());
    }

    public String createFullUrl(String youtubeId, String listId) {
        return listId == null ? youtubeId : youtubeId + "&list=" + listId;
    }
}
