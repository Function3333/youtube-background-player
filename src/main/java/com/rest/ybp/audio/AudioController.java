package com.rest.ybp.audio;

import com.rest.ybp.common.Result;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AudioController {
    private AudioService audioService;

    @Autowired
    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @GetMapping("/audio")
    public String test(@RequestParam("url") String url) {
        System.out.println("url = " + url);
        Result result = audioService.getAudio(url);
        return result.getMsg();
    }
}
